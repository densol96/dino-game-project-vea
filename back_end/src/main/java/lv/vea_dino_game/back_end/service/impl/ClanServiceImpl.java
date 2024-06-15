package lv.vea_dino_game.back_end.service.impl;

import jakarta.transaction.Transactional;
import lv.vea_dino_game.back_end.exceptions.EmptyClanException;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.service.IClanFilterService;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClanServiceImpl implements IClanFilterService {

    private final IClanRepo clanRepo;

    @Override
    public List<Clan> retriveAll() {
      if(clanRepo.count() == 0)
        throw new EmptyDataBaseTable("There are no any clans for display");
      /*
       * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
       */
      return clanRepo.findAll();
    }

    @Override
    public List<Clan> retriveAllByMinEntryLevel(Integer level){
      if (clanRepo.count() == 0)
        throw new EmptyDataBaseTable("There are no any clans for display");
      
      List<Clan> allClans = clanRepo.findAllByMinPlayerLevelGreaterThanEqual(level);
      if (allClans.isEmpty())
        throw new EmptyDataBaseTable("There are no any clans with the minimum entry level of " + level + " or higher for display");
      return allClans;
    }


    @Override
    public Clan retriveClanById(Integer id){
        if (clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");

        Clan clan = clanRepo.findById(id);
        if (clan == null)
            throw new EmptyDataBaseTable("There is no clan with id " + id);
        return clan;
    }

    @Override
    public List<Clan> retriveAllSorteredByMinLevelDesc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllSortedByMinPlayerLevelDesc();
    }

    @Override
    public List<Clan> retriveAllSorteredByMinLevelAsc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllSortedByMinPlayerLevelAsc();
    }

    @Override
    public List<Clan> retriveAllSorteredByTitleDesc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllByOrderByTitleDesc();
    }

    @Override
    public List<Clan> retriveAllSorteredByTitleAsc() {
        if(clanRepo.count() == 0)
            throw new EmptyDataBaseTable("There are no any clans for display");
        /*
         * Once we start developing UI and consuming API on the React side, we will add dto-mapper logic here
         */
        return clanRepo.findAllByOrderByTitleAsc();
    }

    @Override
    public Clan createClan(Clan clan) {
        if (clan == null) throw new EmptyClanException("Clan is empty");
        Clan newClan = clanRepo.findByTitle(clan.getTitle());
        if (newClan!= null) throw new EmptyClanException("Clan with such title already exists");
        return clanRepo.save(clan);
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
