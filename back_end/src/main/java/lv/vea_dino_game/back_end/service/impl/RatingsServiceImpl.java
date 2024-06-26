package lv.vea_dino_game.back_end.service.impl;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.PlayerForRatingsDto;
import lv.vea_dino_game.back_end.model.dto.PublicUserDto;
import lv.vea_dino_game.back_end.model.enums.DinoType;
import lv.vea_dino_game.back_end.model.enums.DinoTypeFilterEnum;
import lv.vea_dino_game.back_end.model.enums.PlayersSortByEnum;
import lv.vea_dino_game.back_end.model.enums.SortDirectionEnum;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IRatingsService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;

@Service
@RequiredArgsConstructor
public class RatingsServiceImpl implements IRatingsService {
  
  private static final Integer RESULTS_PER_PAGE = 5;

  private final IPlayerRepo playerRepo;
  private final IUserRepo userRepo;
  private final Mapper mapper;
  

  @Override
  public List<PlayerForRatingsDto> getPlayers(Integer page, String sortBy, String dinoTypeFilter, String sortDirection) {
    if (page == null || page < 1)
      throw new InvalidUserInputException("Invalid user input for the page of " + page);

    PlayersSortByEnum sortByEnum = extractSortByEnum(sortBy);
    SortDirectionEnum sortDirectionEnum = extractSortDirectionEnum(sortDirection);

    // toString representation of PlayersSortByEnum is exactly the required field for sorting
    Sort sort = Sort.by(sortByEnum.toString());
    Pageable pageable = PageRequest.of(page - 1, RESULTS_PER_PAGE,
        sortDirectionEnum == SortDirectionEnum.DESC ? sort.descending() : sort.ascending());

    DinoTypeFilterEnum typeFilter = extractDinoTypeFilterEnum(dinoTypeFilter);

    List<Player> results = null;

    if (typeFilter == DinoTypeFilterEnum.ALL) {
      results = playerRepo.findAll(pageable).getContent();
    } else {
      results = playerRepo.findAllByDinoType(
          typeFilter == DinoTypeFilterEnum.CARNIVORE ? DinoType.carnivore : DinoType.herbivore, pageable);
    }

    return results.stream().map(player -> mapper.fromPlayerToForRatingsDto(player)).toList();

  }

  @Override
  public Integer getNumberOfPages(String dinoTypeFilter) {
    DinoTypeFilterEnum typeFilter = extractDinoTypeFilterEnum(dinoTypeFilter);
    int resultsTotal;

    if(typeFilter == DinoTypeFilterEnum.ALL) {
       resultsTotal = (int) playerRepo.count();
    } else {
      resultsTotal = (int) playerRepo.countAllByDinoType(typeFilter == DinoTypeFilterEnum.CARNIVORE ? DinoType.carnivore : DinoType.herbivore);
    }
    
    return (int) Math.ceil((double) resultsTotal / RESULTS_PER_PAGE);
  }

  @Override
  public PublicUserDto getPublicUserProfile(Integer id) {
    if (id == null || id < 0)
      throw new InvalidUserInputException("Invalid user id of " + id);
    User user = userRepo.findById(id)
        .orElseThrow(() -> new InvalidUserInputException("There is no user with the id of " + id));
    return mapper.fromUserToPublicDto(user);
  }


  private PlayersSortByEnum extractSortByEnum(String sortBy) {
    try {
      return PlayersSortByEnum.valueOf(sortBy.toUpperCase());
    } catch (Exception e) {
      throw new InvalidUserInputException("Invalid sortBy input of " + sortBy);
    }
  }
  
  private SortDirectionEnum extractSortDirectionEnum(String sortDirection) {
    // ASC / DESC
    try {
      return SortDirectionEnum.valueOf(sortDirection.toUpperCase());
    } catch (Exception e) {
      throw new InvalidUserInputException("Invalid sortDirection input of " + sortDirection);
    }
  }
  
  private DinoTypeFilterEnum extractDinoTypeFilterEnum(String dinoTypeFilter) {
    try {
      return DinoTypeFilterEnum.valueOf(dinoTypeFilter.toUpperCase());
    } catch (Exception e) {
      throw new InvalidUserInputException("Invalid dinoTypeFilter input of " + dinoTypeFilter);
    }
  }
}
