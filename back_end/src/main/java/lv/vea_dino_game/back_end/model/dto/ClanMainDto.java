package lv.vea_dino_game.back_end.model.dto;

import lv.vea_dino_game.back_end.model.Announcement;
import lv.vea_dino_game.back_end.model.Player;

import java.util.List;

public record ClanMainDto(
        Integer id,
        String title,
        String description,
        Integer maxCapacity,
        Integer minPlayerLevel,
        List<Player> players,
        List<Announcement> announcements,
        Player admin
) {

}
