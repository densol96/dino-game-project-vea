package lv.vea_dino_game.back_end.service.impl;

import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.repo.IClanRepo;
import lv.vea_dino_game.back_end.service.IClanFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ClanServiceImpl implements IClanFilterService {
    @Autowired
    private IClanRepo clanRepo;

    @Override
    public ArrayList<Clan> retriveAll() throws Exception {

        ArrayList<Clan> allClans = (ArrayList<Clan>) clanRepo.findAll();
        if (allClans.isEmpty())
            throw new Exception("There is no one clan");
       return allClans;
    }

    @Override
    public ArrayList<Clan> retriveAllByMinEntryLevel(int level) throws Exception {
        if (clanRepo.count() == 0) throw new Exception("There is no one clan");
        ArrayList<Clan> allClans = clanRepo.findAllByMinPlayerLevelGreaterThanEqual(level);
        if (allClans.isEmpty()) throw new Exception("There is no clans that is grater or equal " + level + " level");
        return allClans;
    }




}
