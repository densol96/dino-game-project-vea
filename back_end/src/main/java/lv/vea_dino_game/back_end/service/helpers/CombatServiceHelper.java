package lv.vea_dino_game.back_end.service.helpers;

import lv.vea_dino_game.back_end.model.Combat;
import lv.vea_dino_game.back_end.model.Player;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CombatServiceHelper {

    private boolean allowLogsForDebug = false; // set to false for hiding prints(logs)

    public Combat simulateCombat(Player initiator, Player defender) {
        final Integer level = initiator.getLevel();
        final Integer turnsAmount = 20; // level * 5 ? or const?

        double health_initiator = initiator.getPlayerStats().getHealth();
        Integer endurance_initiator = initiator.getPlayerStats().getEndurance(); // not sure about that prop
        Integer agility_initiator = initiator.getPlayerStats().getAgility();
        Integer damage_initiator = initiator.getPlayerStats().getDamage();
        Integer armor_initiator = initiator.getPlayerStats().getArmor();
        Integer critical_hit_percentage_initiator = initiator.getPlayerStats().getCriticalHitPercentage();

        double health_defender = defender.getPlayerStats().getHealth();
        Integer endurance_defender = defender.getPlayerStats().getEndurance();;
        Integer agility_defender = defender.getPlayerStats().getAgility();;
        Integer damage_defender = defender.getPlayerStats().getDamage();;
        Integer armor_defender = defender.getPlayerStats().getArmor();;
        Integer critical_hit_percentage_defender = defender.getPlayerStats().getCriticalHitPercentage();;

        // not break but return some kind of result data
        for (int i = 0; i < turnsAmount; i++) {
            if (allowLogsForDebug) System.out.println("initiator health: " + health_initiator + "   defender health: " + health_defender);
            health_defender -= calculateDamage(damage_initiator, armor_defender, agility_defender, critical_hit_percentage_initiator);
            if (health_defender <= 0) {
                if (allowLogsForDebug) System.out.println("Initiator won before all the turns resolved");
                if (allowLogsForDebug) System.out.println("initiator health: " + health_initiator + "   defender health: " + health_defender);

                return null;
            }
            health_initiator -= calculateDamage(damage_defender, armor_initiator, agility_initiator, critical_hit_percentage_defender);
            if (health_initiator <= 0) {
                if (allowLogsForDebug) System.out.println("Defender won before all the turns resolved");
                if (allowLogsForDebug) System.out.println("initiator health: " + health_initiator + "   defender health: " + health_defender);
                return null;
            }
        }
        if (allowLogsForDebug) System.out.println("Fight went through all the distance!");
        if (allowLogsForDebug) System.out.println(health_defender >= health_initiator ? "Defender won" : "Initiator won");

        return null;

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
        Random random = new Random();

        double randomPercentage = 0.05; // 5%
        double offset = damage * randomPercentage;
        double min = damage - offset;
        double max = damage + offset;

        double randomNum = min + (max - min) * random.nextDouble();
        if (allowLogsForDebug) System.out.println("getRandomDamage: " + damage + " " + randomNum);
        return randomNum;
    }

    private double getRandomArmor (int armor) {
        Random random = new Random();

        double randomPercentage = 0.03; // 3%
        double offset = armor * randomPercentage;
        double min = armor - offset;
        double max = armor + offset;

        double result = min + (max - min) * random.nextDouble();
        if (allowLogsForDebug) System.out.println("randomArmor: " + armor + " " + result);
        return result;
    }

    private boolean isCritical (double probability) {
        Random random = new Random();
        double randomValue = random.nextDouble();
        boolean result = randomValue < probability;
        if (allowLogsForDebug) System.out.println("isCritical: " + probability + " " + result);
        return result;
    }
}
