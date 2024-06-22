package lv.vea_dino_game.back_end.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import lv.vea_dino_game.back_end.model.dto.PlayerForRatingsDto;
import lv.vea_dino_game.back_end.model.dto.PublicUserDto;

public interface IRatingsService {
  List<PlayerForRatingsDto> getPlayers(Integer page, String sortBy, String dinoTypeFilter, String sortDirection);

  Integer getNumberOfPages(String dinoTypeFilter);

  PublicUserDto getPublicUserProfile(Integer id);
}
