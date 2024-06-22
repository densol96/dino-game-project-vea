package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.enums.DinoType;

public record PlayerInfoDto (
        Integer id,
        String username,
        DinoType dinoType,
        Integer level,
        Integer experience,
        String description
) {
}
