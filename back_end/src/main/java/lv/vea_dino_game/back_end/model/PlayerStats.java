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

    @Min(0)
    @Max(100)
    private Integer health = 1;

    @Min(0)
    @Max(100)
    private Integer endurance = 1;

    @Min(0)
    @Max(100)
    private Integer agility = 1;

    @Min(0)
    @Max(100)
    private Integer damage = 1;

    @Min(0)
    @Max(100)
    private Integer armor = 1;

    @Min(0)
    @Max(100)
    private Integer criticalHitPercentage = 5;

    @OneToOne(mappedBy = "playerStats")
    @ToString.Exclude
    public Player player;

    public PlayerStats(Integer healthPoints, Integer endurancePoints, Integer agilityPoints, Integer damagePoints, Integer armorPoints, Integer criticalHitPercentage){
        setHealth(healthPoints);
        setEndurance(endurancePoints);
        setAgility(agilityPoints);
        setDamage(damagePoints);
        setArmor(armorPoints);
        setCriticalHitPercentage(criticalHitPercentage);
    }
}
