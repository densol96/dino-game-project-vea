package lv.vea_dino_game.back_end.model.dto;


import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.enums.DinoType;

import java.util.ArrayList;
import java.util.List;

public record ClanDto(
        Integer id,
        String title,
        String description,
        DinoType dinoType,
        Integer maxCapacity,
        Integer minPlayerLevel,
        List<AllPlayerInfoDto> players,
        List<String> usernames,
        String adminUsername,
        Integer adminId
) {

}
