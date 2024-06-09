package lv.vea_dino_game.back_end.model.dto;

import java.util.HashMap;

public record ErrorResponse(
  String type,
  String name,
  String message,
  HashMap<String, String> errors    
) {
  
}
