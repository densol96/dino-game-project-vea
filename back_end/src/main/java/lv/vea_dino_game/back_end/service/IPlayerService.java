package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;

import java.util.List;

public interface IPlayerService {
    BasicMessageResponse joinClan(Integer clanId);

    BasicMessageResponse exitClan();


    List<Player> getAllPlayersSortByLevelDesc();

    List<Player> getAllPlayersSortByLevelAsc();

    List<Player> getAllPlayersByLevel(Integer level);
}
