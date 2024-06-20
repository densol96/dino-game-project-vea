package lv.vea_dino_game.back_end.service.helpers;

import org.springframework.stereotype.Service;

import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;

@Service
public class Mapper {

  public UserMainDTO fromUserToDto(User user) {
    return new UserMainDTO(
      user.getId(),
      user.getUsername(),
      user.getEmail(),
      user.getPlayer().getClan() != null ? user.getPlayer().getClan().getTitle() : null,
      user.getPlayer().getPlayerStats(),
      user.getPlayer().getDinoType(),
      user.getPlayer().getLevel()
    );
    
  }
}
