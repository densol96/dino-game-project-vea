package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Player;

import java.util.List;

public interface IPlayerService {
    void joinClan(Integer playerId, Integer clanId);

    void exitClan(Integer playerId);


    List<Player> getAllPlayersSortByLevelDesc();

    List<Player> getAllPlayersSortByLevelAsc();

    List<Player> getAllPlayersByLevel(Integer level);
}
