package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Clan;

import java.util.List;

public interface IClanFilterService {
    List<Clan> retriveAll();

    List<Clan> retriveAllByMinEntryLevel(Integer level);

    Clan retriveClanById(Integer id);

    List<Clan> retriveAllSorteredByMinLevelDesc();

    List<Clan> retriveAllSorteredByMinLevelAsc();

    List<Clan> retriveAllSorteredByTitleDesc();

    List<Clan> retriveAllSorteredByTitleAsc();

    Clan createClan(Clan clan);
}
