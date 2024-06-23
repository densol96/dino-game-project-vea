package lv.vea_dino_game.back_end.model.dto;

import jakarta.validation.constraints.*;

public record CreateClanDto (
    @NotBlank(message = "Title cannot be blank/null")
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    String title,

    @NotBlank(message = "Description cannot be blank/null")
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    String description,

    @Min(value = 0, message = "Maximum capacity can not be negative number")
    @Max(value = 100, message = "Maximum capacity can not be greater than 100")
    @NotNull(message = "Max capacity cannot be null")
    Integer maxCapacity,

    @Min(value = 0, message = "Minimum player level can not be negative number")
    @Max(value = 100, message = "Minimum player level can not be greater than 100")
    @NotNull(message = "Min player level cannot be null")
    Integer minPlayerLevel
){

}
