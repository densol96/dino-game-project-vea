package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Clan;

import java.util.ArrayList;

public interface IClanFilterService {
    ArrayList<Clan> retriveAll() throws Exception;

    ArrayList<Clan> retriveAllByMinEntryLevel(int level) throws Exception;
}
