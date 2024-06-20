package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Clan;

import java.util.List;

public interface IClanFilterService {
    List<Clan> retrieveAll();

    List<Clan> retrieveAllByMinEntryLevel(Integer level);

    Clan retrieveClanById(Integer id);

    List<Clan> retrieveAllSortedByMinLevelDesc();

    List<Clan> retrieveAllSortedByMinLevelAsc();

    List<Clan> retrieveAllSortedByTitleDesc();

    List<Clan> retrieveAllSortedByTitleAsc();

    Clan createClan(Clan clan);

    Clan updateClan(Integer id, Clan updatedClan);

    Clan deleteClan(Integer id);
}
