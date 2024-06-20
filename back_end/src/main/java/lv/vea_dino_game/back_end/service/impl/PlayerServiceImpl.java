package lv.vea_dino_game.back_end.service.impl;

import jakarta.transaction.Transactional;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidClanException;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.dto.AllPlayerInfoDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.PlayerInfoDto;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements IPlayerService {

    private final IPlayerRepo playerRepo;

    private final IClanRepo clanRepo;

    private final IAuthService authService;

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

        Player player = playerRepo.findById(id).get();
        if (player == null)
            throw new EmptyDataBaseTable("There are no players");
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


}
