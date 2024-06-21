package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.enums.DinoType;

import java.util.List;

public record ClanDto(
        Integer id,
        String title,
        String description,
        DinoType dinoType,
        Integer maxCapacity,
        Integer minPlayerLevel,
        List<Player> players,
        String admin
) {

}
