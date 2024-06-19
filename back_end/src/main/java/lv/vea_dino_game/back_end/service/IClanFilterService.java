package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.CreateClanDto;

import java.util.List;

public interface IClanFilterService {
    List<Clan> retrieveAll();

    List<Clan> retrieveAllByMinEntryLevel(Integer level);

    Clan retrieveClanById(Integer id);

    List<Clan> retrieveAllSortedByMinLevelDesc();

    List<Clan> retrieveAllSortedByMinLevelAsc();

    List<Clan> retrieveAllSortedByTitleDesc();

    List<Clan> retrieveAllSortedByTitleAsc();

    BasicMessageResponse createClan(CreateClanDto clanDto);

    BasicMessageResponse updateClan(CreateClanDto updatedClanDto);

    BasicMessageResponse deleteClan();
}
