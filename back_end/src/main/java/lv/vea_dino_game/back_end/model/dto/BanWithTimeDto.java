package lv.vea_dino_game.back_end.model.dto;

import java.time.LocalDateTime;

public record BanWithTimeDto(
        String title,
        String message,
        LocalDateTime date
) {
}
