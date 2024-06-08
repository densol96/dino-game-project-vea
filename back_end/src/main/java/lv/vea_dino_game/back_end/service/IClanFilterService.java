package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Clan;

import java.util.List;

public interface IClanFilterService {
    List<Clan> retriveAll();

    List<Clan> retriveAllByMinEntryLevel(Integer level);

    Clan retriveClanById(int id);
}
