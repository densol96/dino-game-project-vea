package lv.vea_dino_game.back_end.model.dto;

import java.time.LocalDateTime;

public record NewsDto(
        Integer id,
        String title,
        String content,
        LocalDateTime date
) {
}
