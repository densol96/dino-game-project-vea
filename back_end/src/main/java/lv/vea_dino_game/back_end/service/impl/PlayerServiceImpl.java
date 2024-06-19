package lv.vea_dino_game.back_end.service.impl;

import jakarta.transaction.Transactional;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidClanException;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IPlayerService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements IPlayerService {

    private final IPlayerRepo playerRepo;

    private final IClanRepo clanRepo;

    private final Mapper mapper;

    @Override

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


        playerRepo.save(player);
        clanRepo.save(clan);
    }

    @Override
    public void exitClan(Integer playerId) {

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


}
