package lv.vea_dino_game.back_end.service.impl;

import jakarta.transaction.Transactional;
import lv.vea_dino_game.back_end.exceptions.EmptyClanException;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.exceptions.ServiceCurrentlyUnavailableException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.CreateClanDto;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IClanFilterService;

import lv.vea_dino_game.back_end.service.IPlayerService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClanServiceImpl implements IClanFilterService {

    private final IClanRepo clanRepo;

    private final IPlayerRepo playerRepo;

    private final IUserRepo userRepo;

    private final IPlayerService playerService;

    private final AuthServiceImpl authService;
    private final Mapper mapper;


    @Override
    public List<Clan> retrieveAll() {
      if(clanRepo.count() == 0)
        throw new EmptyDataBaseTable("There are no any clans for display");
      /*
       * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
       */
      return clanRepo.findAll();
    }

    @Override
    public List<Clan> retrieveAllByMinEntryLevel(Integer level){
      if (clanRepo.count() == 0)
        throw new EmptyDataBaseTable("There are no any clans for display");
      
      List<Clan> allClans = clanRepo.findAllByMinPlayerLevelGreaterThanEqual(level);
      if (allClans.isEmpty())
        throw new EmptyDataBaseTable("There are no any clans with the minimum entry level of " + level + " or higher for display");
      return allClans;
    }


    @Override
    public Clan retrieveClanById(Integer id){
        if (clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");

        Clan clan = clanRepo.findById(id);
        if (clan == null)
            throw new EmptyDataBaseTable("There is no clan with id " + id);
        return clan;
    }

    @Override
    public List<Clan> retrieveAllSortedByMinLevelDesc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllSortedByMinPlayerLevelDesc();
    }

    @Override
    public List<Clan> retrieveAllSortedByMinLevelAsc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllSortedByMinPlayerLevelAsc();
    }

    @Override
    public List<Clan> retrieveAllSortedByTitleDesc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllByOrderByTitleDesc();
    }

    @Override
    public List<Clan> retrieveAllSortedByTitleAsc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllByOrderByTitleAsc();
    }

    @Override
    public BasicMessageResponse createClan(CreateClanDto clanDto) {
        UserMainDTO user = authService.getMe();
        Player player = playerRepo.findByUserId(user.id());
        if (player == null)
            throw new InvalidPlayerException("No player");
        Integer id = player.getId();
        Player admin = playerRepo.findById(id).get();
        if (admin == null)
            throw new InvalidPlayerException("No player");
        if (clanDto == null) throw new EmptyClanException("Clan is empty");
        Clan clan = new Clan(clanDto.title(), clanDto.description(), clanDto.maxCapacity(), clanDto.minPlayerLevel());
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
        Integer id = player.getId();
        Player admin = playerRepo.findById(id).get();
        if (admin == null)
            throw new InvalidPlayerException("No player");
        Clan clan = clanRepo.findByAdmin(admin);
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
        Integer id = player.getId();
        Player admin = playerRepo.findById(id).get();
        if (admin == null)
            throw new InvalidPlayerException("No player");
        Clan clan = clanRepo.findByAdmin(admin);
        if (clan == null)
            throw new InvalidPlayerException("You are not admin to update the clan!");
        if (clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans to delete");
        clanRepo.delete(clan);
        return new BasicMessageResponse("Clan "+ clan.getTitle() +" has been successfully deleted!");
    }


}
