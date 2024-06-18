package lv.vea_dino_game.back_end.model.dto;


import jakarta.validation.constraints.Size;

public record DescriptionDto(
  
  @Size(max = 300, message = "Description cannot be longer than 300 chars")
  String description
) {
}
