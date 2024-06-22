package lv.vea_dino_game.back_end.service.helpers;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.CombatHistoryRecap;
import lv.vea_dino_game.back_end.model.CombatResult;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.enums.EnumCombatResultType;
import lv.vea_dino_game.back_end.repo.ICombatHistoryRecapRepo;
import lv.vea_dino_game.back_end.repo.ICombatRepo;
import lv.vea_dino_game.back_end.repo.ICombatResultRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CombatServiceHelper {

    private final boolean allowLogsForDebug = true; // set to false for hiding prints(logs)

    private final ICombatRepo combatRepo;
    private final ICombatResultRepo combatResultRepo;
    private final ICombatHistoryRecapRepo combatHistoryRecapRepo;

    private final Random random = new Random(); // Reusable Random instance

    public Combat simulateCombat(Player initiator, Player defender) {
        final Integer level = initiator.getLevel();
        final int turnsAmount = 20; // level * 5 ? or const?

        double health_initiator = initiator.getPlayerStats().getHealth();
        //Integer endurance_initiator = initiator.getPlayerStats().getEndurance(); // not sure about that prop
        Integer agility_initiator = initiator.getPlayerStats().getAgility();
        Integer damage_initiator = initiator.getPlayerStats().getDamage();
        Integer armor_initiator = initiator.getPlayerStats().getArmor();
        Integer critical_hit_percentage_initiator = initiator.getPlayerStats().getCriticalHitPercentage();

        double health_defender = defender.getPlayerStats().getHealth();
        //Integer endurance_defender = defender.getPlayerStats().getEndurance();
        Integer agility_defender = defender.getPlayerStats().getAgility();
        Integer damage_defender = defender.getPlayerStats().getDamage();
        Integer armor_defender = defender.getPlayerStats().getArmor();
        Integer critical_hit_percentage_defender = defender.getPlayerStats().getCriticalHitPercentage();

        Player winner;
        Player loser;


        for (int i = 0; i < turnsAmount; i++) {
            if (allowLogsForDebug) System.out.println("initiator health: " + health_initiator + "   defender health: " + health_defender);
            health_defender -= calculateDamage(damage_initiator, armor_defender, agility_defender, critical_hit_percentage_initiator);
            if (health_defender <= 0) {
                if (allowLogsForDebug) System.out.println("Initiator won before all the turns resolved: " + (i+1) + "/" + turnsAmount);
                if (allowLogsForDebug) System.out.println("initiator health: " + health_initiator + "   defender health: " + health_defender);
                break;
            }
            health_initiator -= calculateDamage(damage_defender, armor_initiator, agility_initiator, critical_hit_percentage_defender);
            if (health_initiator <= 0) {
                if (allowLogsForDebug) System.out.println("Defender won before all the turns resolved: " + (i+1) + "/" + turnsAmount);
                if (allowLogsForDebug) System.out.println("initiator health: " + health_initiator + "   defender health: " + health_defender);
                break;
            }
        }
        if (allowLogsForDebug) System.out.println(health_defender >= health_initiator ? "Defender won" : "Initiator won");
        if (health_defender >= health_initiator) {
            winner = defender;
            loser = initiator;
        } else {
            winner = initiator;
            loser = defender;
        }

        Combat combat = new Combat();
        CombatResult combatResult = new CombatResult();
        combat.setInitiator(initiator);
        combat.setDefender(defender);
        combat.setDateTime(LocalDateTime.now());
        combat.setLevel(level);
        combat.setMaxTurnsAmount(turnsAmount);

        combatResult.setCombatResultType(EnumCombatResultType.VICTORY); // draw not implemented (most likely not needed because it doesn't make any sense to compare left-over HP of double datatype
        combatResult.setWinner(winner);
        combatResult.setLoser(loser);
        combatResult.setWinnerExpReward(50); // magic numbers (fix later)
        combatResult.setWinnerCurrencyChange(10);
        combatResult.setLoserCurrencyChange(-10);
        combatResult.setCombat(combat);
        combat.setCombatResult(combatResult);

        // not implemented so far
        CombatHistoryRecap combatHistoryRecap = new CombatHistoryRecap();
        combatHistoryRecap.setCombat(combat);

        combatRepo.save(combat);
        combatResultRepo.save(combatResult);
        combatHistoryRecapRepo.save(combatHistoryRecap);

        return combat;

    }

    private double calculateDamage (
            int damage,
            int armor,
            int agility,
            int criticalHitPercentage
    ) {
        if (isCritical((double) agility /100)) {
            if (allowLogsForDebug) System.out.println("Miss! " + agility);
            return 0;
        }
        double result = getRandomDamage(damage) * (100 - getRandomArmor(armor))/100;
        if (isCritical((double) criticalHitPercentage /100)) result *= 2;
        if (allowLogsForDebug) System.out.println("calculateDamage: " + damage + " " + result);
        return result;

    }

    private double getRandomDamage (int damage) {
        double randomPercentage = 0.05; // 5%
        double offset = damage * randomPercentage;
        double min = damage - offset;
        double max = damage + offset;

        double randomNum = min + (max - min) * random.nextDouble();
        if (allowLogsForDebug) System.out.println("getRandomDamage: " + damage + " " + randomNum);
        return randomNum;
    }

    private double getRandomArmor (int armor) {
        double randomPercentage = 0.03; // 3%
        double offset = armor * randomPercentage;
        double min = armor - offset;
        double max = armor + offset;

        double result = min + (max - min) * random.nextDouble();
        if (allowLogsForDebug) System.out.println("randomArmor: " + armor + " " + result);
        return result;
    }

    private boolean isCritical (double probability) {
        double randomValue = random.nextDouble();
        boolean result = randomValue < probability;
        if (allowLogsForDebug) System.out.println("isCritical: " + probability + " " + result);
        return result;
    }
}