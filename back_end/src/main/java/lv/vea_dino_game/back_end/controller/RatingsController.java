package lv.vea_dino_game.back_end.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.PlayerForRatingsDto;
import lv.vea_dino_game.back_end.model.dto.PublicUserDto;
import lv.vea_dino_game.back_end.service.IRatingsService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/ratings")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class RatingsController {
  
  private final IRatingsService service;

  @GetMapping("/players")
  public List<PlayerForRatingsDto> getPlayersRatings(
      @RequestParam("page") Integer page,
      @RequestParam("sortBy") String sortBy,
      @RequestParam("dinoType") String dinoType,
      @RequestParam("sortDirection") String sortDirection 
  ) {
    return service.getPlayers(page, sortBy, dinoType, sortDirection);
  }
  
  @GetMapping("/pages-total")
  public Integer getPagesTotal(
      @RequestParam("dinoType") String dinoType
  ) {
    return service.getNumberOfPages(dinoType);
  }
  
  @GetMapping("/users/{id}")
  public PublicUserDto getPlayers(@PathVariable Integer id) {
    return service.getPublicUserProfile(id);
  }
  
  
}
