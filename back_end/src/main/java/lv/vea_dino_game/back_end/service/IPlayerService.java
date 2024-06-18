package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.RequestLearnNewPlayerStats;
import lv.vea_dino_game.back_end.model.dto.RequestStartJob;

import java.util.List;

public interface IPlayerService {
    // clans
    void joinClan(Integer playerId, Integer clanId);
    void enrollClan(Integer playerId);

    List<Player> getAllPlayersSortByLevelDesc();
    List<Player> getAllPlayersSortByLevelAsc();

    List<Player> getAllPlayersByLevel(Integer level);

    PlayerStats getPlayerStatsByPlayerId(Integer id);

    BasicMessageResponse updateSkillPoints(RequestLearnNewPlayerStats requestLearnNewPlayerStats);

    // job
    BasicMessageResponse startJob(RequestStartJob requestStartJob);
    BasicMessageResponse finishJob(Integer id);

}
