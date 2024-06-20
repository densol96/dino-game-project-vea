package lv.vea_dino_game.back_end.service.impl;

import jakarta.transaction.Transactional;
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
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements IPlayerService {

    private final IPlayerRepo playerRepo;

    private final IClanRepo clanRepo;

    @Override
    @Transactional
    public void joinClan(Integer playerId, Integer clanId) {
        Clan clan = clanRepo.findById(clanId);
        if (clan == null) {
            throw new InvalidClanException("Clan does not exist");
        }
        Player player = playerRepo.findById(playerId).get();
        if (player == null) {
            throw new InvalidClanException("Player does not exist");
        }
        if (player.getClan()!=null)
            throw new InvalidClanException("Player is already in a clan, you must enroll from your current clan");
        if (clan.getPlayers().size() >= clan.getMaxCapacity()) {
            throw new IllegalStateException("Clan is already at maximum capacity");
        }

        if (player.getLevel() < clan.getMinPlayerLevel()) {
            throw new IllegalStateException("Player does not meet the minimum level requirement for the clan");
        }
        player.setClan(clan);
        //clan.getPlayers().add(player);
        playerRepo.save(player);
        clanRepo.save(clan);
    }

    @Override
    public void enrollClan(Integer playerId) {
        Player player = playerRepo.findById(playerId).get();
        if (player == null) {
            throw new InvalidClanException("Player does not exist");
        }
        Clan clan = player.getClan();
        if (player.getClan()==null)
            throw new InvalidPlayerException("Player already is not in a clan");
        player.setClan(null);
        playerRepo.save(player);
        clanRepo.save(clan);
    }

    @Override
    public List<Player> getAllPlayersSortByLevelDesc() {
        if (clanRepo.count() == 0){
            throw new EmptyDataBaseTable("There are no any players for display");
        }
        List<Player> player = playerRepo.findAllByOrderByLevelDesc();
        if (player == null)
            throw new EmptyDataBaseTable("There are no any players for display");
        return player;
    }

    @Override
    public List<Player> getAllPlayersSortByLevelAsc() {
        if (playerRepo.count() == 0){
            throw new EmptyDataBaseTable("There are no any players for display");
        }
        List<Player> player =  playerRepo.findAllByOrderByLevelAsc();
        if (player == null)
            throw new EmptyDataBaseTable("There are no any players for display");
        return player;
    }

    @Override
    public List<Player> getAllPlayersByLevel(Integer level) {
        if (playerRepo.count() == 0){throw new EmptyDataBaseTable("There are no players");}

        List<Player> player = playerRepo.findAllByLevel(level);
        if (player == null)
            throw new EmptyDataBaseTable("There are no players");
        return player;
    }

    @Override
    public PlayerStats getPlayerStatsByPlayerId(Integer id) {
        if (!playerRepo.existsById(id)) throw new InvalidPlayerException("Player with id " + id + " does not exist");
        return playerRepo.findById(id).get().getPlayerStats();
    }

    @Override
    public BasicMessageResponse updateSkillPoints(RequestLearnNewPlayerStats requestLearnNewPlayerStats) {
        Integer playerId = requestLearnNewPlayerStats.playerId();
        if (!playerRepo.existsById(playerId)) throw new InvalidPlayerException("Player with id " + playerId + " does not exist");
        Player player = playerRepo.findById(playerId).get();
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
        Player player = playerRepo.findById(playerId).get();
        Job job = new Job();
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
        Player player = playerRepo.findById(id).get();
        Job job = player.getCurrentJob();
        player.setCurrency(player.getCurrency() + job.getRewardCurrency());
        player.setCurrentJob(null);

        playerRepo.save(player);

        return new BasicMessageResponse("Job ended successfully");
    }

}
