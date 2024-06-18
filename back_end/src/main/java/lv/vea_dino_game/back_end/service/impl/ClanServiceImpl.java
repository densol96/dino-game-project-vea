package lv.vea_dino_game.back_end.service.impl;

import jakarta.transaction.Transactional;
import lv.vea_dino_game.back_end.exceptions.EmptyClanException;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.exceptions.ServiceCurrentlyUnavailableException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IClanFilterService;

import lv.vea_dino_game.back_end.service.IPlayerService;
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

    private final IPlayerService playerService;

    private final AuthServiceImpl authService;

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
    public Clan createClan(Clan clan) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails))
            throw new ServiceCurrentlyUnavailableException("Service 'get-me' is temporarily not working");
        User currentUser = (User) auth.getPrincipal();
        System.out.println(currentUser.getPlayer().getId());
        System.out.println("___________________________________");
        Integer id = currentUser.getPlayer().getId();

        Player admin = playerRepo.findById(id).get();
        if (admin == null)
            throw new InvalidPlayerException("No player");
        if (clan == null) throw new EmptyClanException("Clan is empty");
        Clan newClan = clanRepo.findByTitle(clan.getTitle());

        if (newClan!= null) throw new EmptyClanException("Clan with such title already exists");
        clan.setAdmin(admin);
        Clan newClan2 = clanRepo.save(clan);
        Integer id1 = newClan2.getId();
        System.out.println(admin.getId()+ " ____________" + newClan2.getId());

        return clan;
    }


    @Override
    @Transactional
    public Clan updateClan(Integer id, Clan updatedClan) {
        if (clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");

        Clan clan = clanRepo.findById(id);
        if (clan == null)
            throw new EmptyDataBaseTable("There is no clan with id " + id);

        clan.setTitle(updatedClan.getTitle());
        clan.setDescription(updatedClan.getDescription());
        clan.setMinPlayerLevel(updatedClan.getMinPlayerLevel());
        clanRepo.save(clan);
        return clan;
    }

    @Override
    @Transactional
    public Clan deleteClan(Integer id) {
        if (clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");

        Clan clan = clanRepo.findById(id);
        if (clan == null)
            throw new EmptyDataBaseTable("There is no clan with id " + id);
        clanRepo.delete(clan);
        return clan;
    }


}
