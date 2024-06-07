package lv.vea_dino_game.back_end.service.helpers;

import lv.vea_dino_game.back_end.model.Player;

import java.util.Random;

public class CombatServiceHelper {
    public void combatResult() {
        Integer level = 3;
        Integer turnsAmount = 20;

        double health_initiator = 1000;
        Integer endurance_initiator = 20;
        Integer agility_initiator = 3;
        Integer damage_initiator = 25;
        Integer armor_initiator = 24;
        Integer critical_hit_percentage_initiator = 12;

        double health_defender = 1000;
        Integer endurance_defender = 20;
        Integer agility_defender = 5;
        Integer damage_defender = 29;
        Integer armor_defender = 37;
        Integer critical_hit_percentage_defender = 35;

        // not break but return some kind of result data
        for (int i = 0; i < turnsAmount; i++) {
            health_defender -= calculateDamage(damage_initiator, armor_defender, agility_defender, critical_hit_percentage_initiator);
            health_initiator -= calculateDamage(damage_defender, armor_initiator, agility_initiator, critical_hit_percentage_defender);
            if (health_initiator >= 0 && health_defender < 0) {
                System.out.println("Defender won");
                break;
            }
            if (health_defender >= 0 && health_initiator < 0) {
                System.out.println("Initiator won");
                break;
            }
            if (health_initiator < 0 && health_defender < 0) {
                if (health_defender > health_initiator) {
                    System.out.println("Defender won");
                    break;
                } else if (health_defender < health_initiator) {
                    System.out.println("Initiator won");
                    break;
                } else {
                    System.out.println("Draw");
                    break;
                }
            }
        }

    }

    public double calculateDamage (
        int damage,
        int armor,
        int agility,
        int criticalHitPercentage
    ) {
        if (isCritical((double) agility /100)) return 0;
        double result = getRandomDamage(damage) * (100 - getRandomArmor(armor))/100;
        if (isCritical((double) criticalHitPercentage /100)) result *= 2;
        return result;

    }

    public double getRandomDamage (int damage) {
        Random random = new Random();

        double randomPercentage = 0.05; // 5%
        double offset = damage * randomPercentage;
        double min = damage - offset;
        double max = damage + offset;

        double randomNum = min + (max - min) * random.nextDouble();
        return randomNum;
    }

    public double getRandomArmor (int armor) {
        Random random = new Random();

        double randomPercentage = 0.03; // 3%
        double offset = armor * randomPercentage;
        double min = armor - offset;
        double max = armor + offset;

        return min + (max - min) * random.nextDouble();
    }

    public boolean isCritical (double probability) {
        Random random = new Random();
        double randomValue = random.nextDouble();
        return randomValue < probability;
    }
}
