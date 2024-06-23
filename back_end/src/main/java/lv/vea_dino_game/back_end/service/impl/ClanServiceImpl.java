package lv.vea_dino_game.back_end.service.impl;

import jakarta.transaction.Transactional;
import lv.vea_dino_game.back_end.exceptions.EmptyClanException;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;

import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;

import lv.vea_dino_game.back_end.model.dto.*;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.model.enums.ClanSortByEnum;
import lv.vea_dino_game.back_end.model.enums.SortDirectionEnum;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IClanFilterService;

import lv.vea_dino_game.back_end.service.IPlayerService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClanServiceImpl implements IClanFilterService {

    private final IClanRepo clanRepo;

    private final IPlayerRepo playerRepo;

    private final AuthServiceImpl authService;
    private final Mapper mapper;
    private static final Integer RESULTS_PER_PAGE = 5;


    @Override
    public List<AllClanInfoViewDto> retrieveAll(Integer page, String sortBy, String sortDirection) {
        if (page == null || page < 1)
            throw new InvalidUserInputException("Invalid user input for the page of " + page);

        ClanSortByEnum sortByEnum = extractSortByEnum(sortBy);
        SortDirectionEnum sortDirectionEnum = extractSortDirectionEnum(sortDirection);

        Sort sort = Sort.by(sortByEnum.toString().toLowerCase());
        Pageable pageable = PageRequest.of(page - 1, RESULTS_PER_PAGE,
                sortDirectionEnum == SortDirectionEnum.DESC ? sort.descending() : sort.ascending());

        List<Clan> results = clanRepo.findAll(pageable).getContent();
        return results.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Integer getNumberOfPages() {
        int resultsTotal = (int) clanRepo.count();
        return (int) Math.ceil((double) resultsTotal / RESULTS_PER_PAGE);
    }

    @Override
    public List<AllClanInfoViewDto> retrieveAllByMinEntryLevel(Integer level){
      if (clanRepo.count() == 0)
        throw new EmptyDataBaseTable("There are no any clans to display");
      
      List<Clan> allClans = clanRepo.findAllByMinPlayerLevelGreaterThanEqual(level);
      if (allClans.isEmpty())
        throw new EmptyDataBaseTable("There are no any clans with the minimum entry level of " + level + " or higher to display");
      return allClans.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }


    @Override
    public ClanDto retrieveClanById(Integer id){
        if (clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans to display");

        Clan clan = clanRepo.findById(id);
        if (clan == null)
            throw new EmptyDataBaseTable("There is no clan with id " + id);
        return mapper.clanToDto(clan);
    }

    @Override
    public List<AllClanInfoViewDto> retrieveAllSortedByMinLevelDesc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans to display");

        List<Clan> allClans = clanRepo.findAllSortedByMinPlayerLevelDesc();
        if (allClans.isEmpty()){
            throw new EmptyDataBaseTable("There are no any clans to display");
        }
        return allClans.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<AllClanInfoViewDto> retrieveAllSortedByMinLevelAsc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans to display");

        List<Clan> allClans = clanRepo.findAllSortedByMinPlayerLevelAsc();
        if (allClans.isEmpty()){
            throw new EmptyDataBaseTable("There are no any clans to display");
        }
        return allClans.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<AllClanInfoViewDto> retrieveAllSortedByTitleDesc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans to display");

        List<Clan> allClans = clanRepo.findAllByOrderByTitleDesc();
        if (allClans.isEmpty()){
            throw new EmptyDataBaseTable("There are no any clan to display");
        }
        return allClans.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<AllClanInfoViewDto> retrieveAllSortedByTitleAsc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans to display");

        List <Clan> allClans = clanRepo.findAllByOrderByTitleAsc();
        if (allClans.isEmpty()){
            throw new EmptyDataBaseTable("There are no any clans to display");
        }
        return allClans.stream().map(mapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public BasicMessageResponse createClan(CreateClanDto clanDto) {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        if (clanDto == null) throw new EmptyClanException("Clan is empty");
        Clan clan = new Clan(clanDto.title(), clanDto.description(), clanDto.maxCapacity(), clanDto.minPlayerLevel());
        if (clan.getMinPlayerLevel() > player.getLevel()){
            clan.setMinPlayerLevel(player.getLevel());
        }
        clan.setDinoType(player.getDinoType());
        clan.setAdmin(player);
        clan.setSinglePlayer(player);
        clanRepo.save(clan);
        return new BasicMessageResponse("Clan " + clanDto.title() + " has been successfully created! Now you are admin of this clan!");
    }
    @Override
    public BasicMessageResponse updateClan(CreateClanDto updatedClanDto) {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Clan clan = clanRepo.findByAdmin(player);
        if (clan == null)
            throw new InvalidPlayerException("You are not admin to update the clan!");
        if (updatedClanDto == null) throw new EmptyClanException("Clan is empty");

        clan.setTitle(updatedClanDto.title());
        clan.setDescription(updatedClanDto.description());
        clan.setMaxCapacity(updatedClanDto.maxCapacity());
        clan.setMinPlayerLevel(updatedClanDto.minPlayerLevel());
        clanRepo.save(clan);
        return new BasicMessageResponse("Clan "+ updatedClanDto.title()+" has been successfully updated!");
    }

    @Override
    public BasicMessageResponse deleteClan() {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Clan clan = clanRepo.findByAdmin(player);
        if (clan == null)
            throw new InvalidPlayerException("You are not admin to update the clan!");
        if (clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans to delete");
        for (Player p : clan.getPlayers()) {
            p.setClan(null);
            playerRepo.save(p);
        }
        clan.setAdmin(null);
        clanRepo.save(clan);
        clanRepo.delete(clan);
        return new BasicMessageResponse("Clan "+ clan.getTitle() +" has been successfully deleted!");
    }

    @Override
    public ClanDto getClanWithMe() {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Clan clan = clanRepo.findByAdmin(player);
        if (clan == null)
            throw new EmptyDataBaseTable("There is no clan");
        return mapper.clanToDto(clan);
    }

    private ClanSortByEnum extractSortByEnum(String sortBy) {
        try {
            return ClanSortByEnum.valueOf(sortBy.toUpperCase());
        } catch (Exception e) {
            throw new InvalidUserInputException("Invalid sortBy input of " + sortBy);
        }
    }

    private SortDirectionEnum extractSortDirectionEnum(String sortDirection) {
        try {
            return SortDirectionEnum.valueOf(sortDirection.toUpperCase());
        } catch (Exception e) {
            throw new InvalidUserInputException("Invalid sortDirection input of " + sortDirection);
        }
    }


}
