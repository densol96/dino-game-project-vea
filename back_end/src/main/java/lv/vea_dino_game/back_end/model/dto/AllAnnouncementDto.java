package lv.vea_dino_game.back_end.model.dto;

import java.time.LocalDateTime;

public record AllAnnouncementDto (
        Integer id,
        String author,
        String title,
        String content,
        LocalDateTime date
) {
}
