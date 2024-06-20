package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.dto.AllPlayerInfoDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.PlayerInfoDto;

import java.util.List;

public interface IPlayerService {
    BasicMessageResponse joinClan(Integer clanId);

    BasicMessageResponse exitClan();


    List<AllPlayerInfoDto> getAllPlayersSortByLevelDesc();

    List<AllPlayerInfoDto> getAllPlayersSortByLevelAsc();

    List<AllPlayerInfoDto> getAllPlayersByLevel(Integer level);

    PlayerInfoDto getPlayerById(Integer id);

    PlayerInfoDto getMyProfile();
}
