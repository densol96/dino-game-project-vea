package lv.vea_dino_game.back_end.model.dto;

public record AllClanInfoViewDto (
    Integer id,
    String title,
    String description,
    String admin,
    Integer minPlayerLevel,
    Integer maxCapacity
) {
}
