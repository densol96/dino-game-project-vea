package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "player_stats")
public class PlayerStats {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Min(value = 0, message = "Health value can not be less than 0")
    @Max(value = 100, message = "Health value can not be greater than 100")
    private Integer health = 1;

    @Min(value = 0, message = "Endurance value can not be less than 0")
    @Max(value = 100, message = "Endurance value can not be greater than 100")
    private Integer endurance = 1;

    @Min(value = 0, message = "Agility value can not be less than 0")
    @Max(value = 100, message = "Agility value can not be greater than 100")
    private Integer agility = 1;

    @Min(value = 0, message = "Damage value can not be less than 0")
    @Max(value = 100, message = "Damage value can not be greater than 100")
    private Integer damage = 1;

    @Min(value = 0, message = "Armor value can not be less than 0")
    @Max(value = 100, message = "Armor value can not be greater than 100")
    private Integer armor = 1;

    @Min(value = 0, message = "Critical hit percentage value can not be less than 0")
    @Max(value = 100, message = "Critical hit percentage value can not be greater than 100")
    private Integer criticalHitPercentage = 5;

    @OneToOne(mappedBy = "playerStats")
    @ToString.Exclude
    public Player player;

    public PlayerStats(Integer healthPoints, Integer endurancePoints, Integer agilityPoints, Integer damagePoints, Integer armorPoints, Integer criticalHitPercentage) {
        setHealth(healthPoints);
        setEndurance(endurancePoints);
        setAgility(agilityPoints);
        setDamage(damagePoints);
        setArmor(armorPoints);
        setCriticalHitPercentage(criticalHitPercentage);
    }
}
