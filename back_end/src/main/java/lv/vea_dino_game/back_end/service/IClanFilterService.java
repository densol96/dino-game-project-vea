package lv.vea_dino_game.back_end.service;


import lv.vea_dino_game.back_end.model.dto.AllClanInfoViewDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.ClanDto;
import lv.vea_dino_game.back_end.model.dto.CreateClanDto;

import java.util.List;

public interface IClanFilterService {
    List<AllClanInfoViewDto> retrieveAll(Integer page, String sortBy, String sortDirection);

    List<AllClanInfoViewDto> retrieveAllByMinEntryLevel(Integer level);

    ClanDto retrieveClanById(Integer id);

    List<AllClanInfoViewDto> retrieveAllSortedByMinLevelDesc();

    List<AllClanInfoViewDto> retrieveAllSortedByMinLevelAsc();

    List<AllClanInfoViewDto> retrieveAllSortedByTitleDesc();

    List<AllClanInfoViewDto> retrieveAllSortedByTitleAsc();

    BasicMessageResponse createClan(CreateClanDto clanDto);

    BasicMessageResponse updateClan(CreateClanDto updatedClanDto);

    BasicMessageResponse deleteClan();

    ClanDto getClanWithMe();

    Integer getNumberOfPages();
}
