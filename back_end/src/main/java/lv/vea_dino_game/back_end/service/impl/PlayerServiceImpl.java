package lv.vea_dino_game.back_end.service.impl;


import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidClanException;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.Job;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.RequestLearnNewPlayerStats;
import lv.vea_dino_game.back_end.model.dto.RequestStartJob;
import lv.vea_dino_game.back_end.model.dto.AllPlayerInfoDto;

import lv.vea_dino_game.back_end.model.dto.PlayerInfoDto;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements IPlayerService {

    private final IPlayerRepo playerRepo;

    private final IClanRepo clanRepo;

    private final IAuthService authService;
    private final IMailService mailService;

    private final Mapper mapper;

    @Override
    public BasicMessageResponse joinClan(Integer clanId) {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Clan clan = clanRepo.findById(clanId);
        if (clan == null) {
            throw new InvalidClanException("Clan does not exist");
        }
        if (player.getClan()!=null)
            throw new InvalidClanException("Player is already in a clan, you must enroll from your current clan");
        if (clan.getPlayers().size() >= clan.getMaxCapacity()) {
            throw new IllegalStateException("Clan is already at maximum capacity");
        }
        if (player.getDinoType() != clan.getDinoType()){
            throw new InvalidClanException("Player dino type does not match clan dino type");
        }
        if (player.getLevel() < clan.getMinPlayerLevel()) {
            throw new IllegalStateException("Player does not meet the minimum level requirement for the clan");
        }
        player.setClan(clan);
        playerRepo.save(player);
        clanRepo.save(clan);

        return new BasicMessageResponse("Congratulations! You successfully have joined clan "+ clan.getTitle() + "!");
    }

    @Override
    public BasicMessageResponse exitClan() {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Clan clan = player.getClan();
        if (player.getClan()==null)
            throw new InvalidPlayerException("Player already is not in a clan");
        player.setClan(null);
        playerRepo.save(player);
        clanRepo.save(clan);
        return new BasicMessageResponse("Congratulations! You successfully have exited clan "+ clan.getTitle() + "!");
    }

    @Override
    public List<AllPlayerInfoDto> getAllPlayersSortByLevelDesc() {
        if (clanRepo.count() == 0){
            throw new EmptyDataBaseTable("There are no any players for display");
        }
        List<Player> player = playerRepo.findAllByOrderByLevelDesc();
        if (player == null)
            throw new EmptyDataBaseTable("There are no any players for display");
        return player.stream().map(mapper::playerToDto).collect(Collectors.toList());
    }

    @Override
    public List<AllPlayerInfoDto> getAllPlayersSortByLevelAsc() {
        if (playerRepo.count() == 0){
            throw new EmptyDataBaseTable("There are no any players for display");
        }
        List<Player> player =  playerRepo.findAllByOrderByLevelAsc();
        if (player == null)
            throw new EmptyDataBaseTable("There are no any players for display");
        return player.stream().map(mapper::playerToDto).collect(Collectors.toList());
    }

    @Override
    public List<AllPlayerInfoDto> getAllPlayersByLevel(Integer level) {
        if (playerRepo.count() == 0){throw new EmptyDataBaseTable("There are no players");}

        List<Player> player = playerRepo.findAllByLevel(level);
        if (player == null)
            throw new EmptyDataBaseTable("There are no players");
        return player.stream().map(mapper::playerToDto).collect(Collectors.toList());
    }

    @Override
    public PlayerInfoDto getPlayerById(Integer id) {
        if (playerRepo.count() == 0){throw new EmptyDataBaseTable("There are no players");}

        Optional<Player> playerOptional = playerRepo.findById(id);
        if (playerOptional.isEmpty()) {
            throw new InvalidPlayerException("Invalid friend");
        }
        Player player = playerOptional.get();
        return mapper.onePlayerToDto(player);
    }

    @Override
    public PlayerInfoDto getMyProfile() {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        return mapper.onePlayerToDto(player);
    }

    @Override
    public PlayerStats getPlayerStatsByPlayerId(Integer id) {
        return playerRepo.findById(id)
                .map(Player::getPlayerStats)
                .orElseThrow(() -> new InvalidPlayerException("Player with id " + id + " does not exist"));
    }

    @Override
    public BasicMessageResponse updateSkillPoints(RequestLearnNewPlayerStats requestLearnNewPlayerStats) {
        Integer playerId = requestLearnNewPlayerStats.playerId();
        if (!playerRepo.existsById(playerId)) throw new InvalidPlayerException("Player with id " + playerId + " does not exist");
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new InvalidPlayerException("Player with id " + playerId + " does not exist"));
        if (player.getCurrency() - requestLearnNewPlayerStats.currencySpent() < 0) throw new InvalidPlayerException("You cant spend more currency than you have, when learning new skill points");

        player.setCurrency(player.getCurrency() - requestLearnNewPlayerStats.currencySpent());

        player.getPlayerStats().setCriticalHitPercentage(requestLearnNewPlayerStats.criticalHitPercentage());
        player.getPlayerStats().setArmor(requestLearnNewPlayerStats.armor());
        player.getPlayerStats().setAgility(requestLearnNewPlayerStats.agility());
        player.getPlayerStats().setHealth(requestLearnNewPlayerStats.health());
        player.getPlayerStats().setDamage(requestLearnNewPlayerStats.damage());
        player.getPlayerStats().setEndurance(requestLearnNewPlayerStats.endurance());

        playerRepo.save(player);

        return new BasicMessageResponse("New player stats has been successfully learned");
    }

    @Override
    public BasicMessageResponse startJob(RequestStartJob requestStartJob) {
        Integer playerId = requestStartJob.playerId();
        if (!playerRepo.existsById(playerId)) throw new InvalidPlayerException("Player with id " + playerId + " does not exist");
        Player player = playerRepo.findById(playerId)
                .orElseThrow(() -> new InvalidPlayerException("Player with id " + playerId + " does not exist"));

        Job job = new Job();
        if (requestStartJob.hoursDuration() == 1 && requestStartJob.rewardCurrency() != 10) throw new InvalidPlayerException("Hack attempt");
        if (requestStartJob.hoursDuration() == 12 && requestStartJob.rewardCurrency() != 40) throw new InvalidPlayerException("Hack attempt");
        job.setRewardCurrency(requestStartJob.rewardCurrency());
        job.setHoursDuration(requestStartJob.hoursDuration());
        player.setCurrentJob(job);
        player.setWorkingUntil(LocalDateTime.now().plusHours(job.getHoursDuration()));


        playerRepo.save(player);

        return new BasicMessageResponse("Job started successfully");
    }

    @Override
    public BasicMessageResponse finishJob(Integer id) {
        if (!playerRepo.existsById(id)) throw new InvalidPlayerException("Player with id " + id + " does not exist");
        Player player = playerRepo.findById(id)
                .orElseThrow(() -> new InvalidPlayerException("Player with id " + id + " does not exist"));
        Job job = player.getCurrentJob();
        player.setCurrency(player.getCurrency() + job.getRewardCurrency());
        player.setCurrentJob(null);

        playerRepo.save(player);

        mailService.sendNotificationFromAdmin(
                player.getUser().getUsername(),
                "Farm",
                "Your farm has been finished successfully. You earned " + job.getRewardCurrency() + " gold."
        );

        return new BasicMessageResponse("Job finished successfully");
    }

}
