package lv.vea_dino_game.back_end.model.dto;

public record RequestLearnNewPlayerStats(
        Integer currencySpent,

        Integer armor,
        Integer agility,
        Integer health,
        Integer damage,
        Integer criticalHitPercentage,
        Integer endurance
) {}
