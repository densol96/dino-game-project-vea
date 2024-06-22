package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;

import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;

import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.ICombatService;
import lv.vea_dino_game.back_end.service.IMailService;
import lv.vea_dino_game.back_end.service.helpers.CombatServiceHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;



@Service
@RequiredArgsConstructor
public class CombatServiceImpl implements ICombatService {

    private final IPlayerRepo playerRepo;
    private final CombatServiceHelper combatServiceHelper;
    private final IMailService mailService;


    private final int MAX_XP_THRESHOLD = 1000;
    private final int MAX_LVL = 10;
    private final int IMMUNITY_IN_HOURS_AFTER_DEFEAT = 8;
    private final int COOL_DOWN_IN_MINUTES_AFTER_ATTACK = 15;

    @Override
    public Combat attackSelectedPlayerOnArena(Integer attackerId, Integer defenderId) {
        if (attackerId == null || !playerRepo.existsById(attackerId)) throw new InvalidPlayerException("attackerId " + attackerId + " does not exist");
        if (defenderId == null || !playerRepo.existsById(defenderId)) throw new InvalidPlayerException("defenderId " + defenderId + " does not exist");
        Player attacker = playerRepo.findById(attackerId).orElseThrow(() -> new InvalidPlayerException("attackerId " + attackerId + " does not exist"));
        Player defender = playerRepo.findById(defenderId).orElseThrow(() -> new InvalidPlayerException("defenderId " + defenderId + " does not exist"));

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
            loser.setCurrency(loser.getCurrency() + combat.getCombatResult().loserCurrencyChange);
        }

        if (defender.getId().equals(combat.getCombatResult().getLoser().getId())) { // when attacker lost he gets no immunity
            loser.setImmuneUntil(LocalDateTime.now().plusHours(IMMUNITY_IN_HOURS_AFTER_DEFEAT));
        }

        // assigning attack cooldown
        if (attacker.getId().equals(combat.getCombatResult().getWinner().getId())) {// winner attacked
            winner.setCannotAttackAgainUntil(LocalDateTime.now().plusMinutes(COOL_DOWN_IN_MINUTES_AFTER_ATTACK));
        } else { // loser attacked
            loser.setCannotAttackAgainUntil(LocalDateTime.now().plusMinutes(COOL_DOWN_IN_MINUTES_AFTER_ATTACK));
        }

        // each player +1 combat total
        winner.getCombatStats().setCombatsTotal(winner.getCombatStats().getCombatsTotal() + 1);
        loser.getCombatStats().setCombatsTotal(loser.getCombatStats().getCombatsTotal() + 1);
        // winner +1 won combat
        winner.getCombatStats().setCombatsWon(winner.getCombatStats().getCombatsWon() + 1);

        // winner += reward
        winner.getCombatStats().setCurrencyWon(winner.getCombatStats().getCurrencyWon() + combat.getCombatResult().winnerCurrencyChange);
        // loser -= reward
        loser.getCombatStats().setCurrencyLost(loser.getCombatStats().getCurrencyLost() + combat.getCombatResult().loserCurrencyChange);

        playerRepo.save(winner);
        playerRepo.save(loser);

        final String winMessage = attacker.getUser().getUsername() + " attacked you. You won the combat and received " + combat.getCombatResult().winnerCurrencyChange + " gold and " + combat.getCombatResult().winnerExpReward + " experience.";
        final String loseMessage = attacker.getUser().getUsername() + " attacked you. You have been defeated and you lost " + (-combat.getCombatResult().loserCurrencyChange) + " gold.";
        final String message = defender.getId().equals(combat.getCombatResult().getWinner().getId()) ? winMessage : loseMessage;

        mailService.sendNotificationFromAdmin(
            defender.getUser().getUsername(),
            "You have been attacked!",
            message
        );

        return combat;
    }
}
