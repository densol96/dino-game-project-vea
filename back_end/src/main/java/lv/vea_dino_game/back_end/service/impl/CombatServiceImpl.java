package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.EmptyDataBaseTable;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Clan;
import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.repo.ICombatRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.ICombatService;
import lv.vea_dino_game.back_end.service.helpers.CombatServiceHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CombatServiceImpl implements ICombatService {

    private final IPlayerRepo playerRepo;
    private final CombatServiceHelper combatServiceHelper;

    private final int MAX_XP_THRESHOLD = 1000;
    private final int MAX_LVL = 10;
    private final int IMMUNITY_IN_HOURS_AFTER_DEFEAT = 8;

    @Override
    public Combat attackSelectedPlayerOnArena(Integer attackerId, Integer defenderId) {
        if (attackerId == null || !playerRepo.existsById(attackerId)) throw new InvalidPlayerException("attackerId " + attackerId + " does not exist");
        if (defenderId == null || !playerRepo.existsById(defenderId)) throw new InvalidPlayerException("defenderId " + defenderId + " does not exist");
        Player attacker = playerRepo.findById(attackerId).get();
        Player defender = playerRepo.findById(defenderId).get();

        Combat combat = combatServiceHelper.simulateCombat(attacker, defender);
        Player winner = combat.getCombatResult().getWinner();
        Player loser = combat.getCombatResult().getLoser();
        winner.setCurrency(winner.getCurrency() + combat.getCombatResult().winnerCurrencyChange);
        winner.setExperience(winner.getExperience() + combat.getCombatResult().winnerExpReward);

        if (winner.getExperience() >= MAX_XP_THRESHOLD && winner.getLevel() < MAX_LVL) { // only increasing lvl for less than max lvl
            winner.setExperience(winner.getExperience() - MAX_XP_THRESHOLD);
            winner.setLevel(winner.getLevel()+1);
        }

        if (loser.getCurrency() + combat.getCombatResult().loserCurrencyChange < 0) { // if loser had less gold than need to be subtracted -> setting to 0
            loser.setCurrency(0);
        } else {
            loser.setCurrency(winner.getCurrency() + combat.getCombatResult().loserCurrencyChange);
        }

        if (defender.getId().equals(combat.getCombatResult().getLoser().getId())) { // when attacker lost he gets no immunity
            loser.setImmuneUntil(LocalDateTime.now().plusHours(IMMUNITY_IN_HOURS_AFTER_DEFEAT));
        }

        playerRepo.save(winner);
        playerRepo.save(loser);

        return combat;
    }
}