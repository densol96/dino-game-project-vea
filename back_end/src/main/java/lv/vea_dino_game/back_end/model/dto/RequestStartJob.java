package lv.vea_dino_game.back_end.model.dto;

public record RequestStartJob(
        Integer playerId,

        Integer hoursDuration,
        Integer rewardCurrency
) {}
