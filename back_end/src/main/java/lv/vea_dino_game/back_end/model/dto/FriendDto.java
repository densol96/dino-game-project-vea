package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.enums.DinoType;


public record FriendDto(
        Integer friendShipId,
        Integer playerId,
        Integer friendId,
        String username,
        DinoType dinoType,
        Integer level,
        String username1,
        DinoType dinoType1,
        Integer level1
) {
}
