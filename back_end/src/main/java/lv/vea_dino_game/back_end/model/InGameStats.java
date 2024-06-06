package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity

@Table(name = "InGameStats")
public class InGameStats {
    @Setter(value = AccessLevel.NONE)
    @Column(name = "IdIn")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Min(0)
    @Max(100)
    @Column(name="HealthPoints")
    private int healthPoints;

    @Min(0)
    @Max(100)
    @Column(name="EndurancePoints")
    private int endurancePoints;

    @Min(0)
    @Max(100)
    @Column(name="AgilityPoints")
    private int agilityPoints;

    @Min(0)
    @Max(100)
    @Column(name="DamagePoints")
    private int damagePoints;

    @Min(0)
    @Max(100)
    @Column(name="ArmorPoints")
    private int armorPoints;

    @Min(0)
    @Max(100)
    @Column(name="CriticalHitPercentage")
    private int criticalHitPercentage;

    @OneToOne(mappedBy = "inGameStats")
    @ToString.Exclude
    public Player player;

    public InGameStats(int healthPoints, int endurancePoints,int agilityPoints, int damagePoints, int armorPoints, int criticalHitPercentage){
        setHealthPoints(healthPoints);
        setEndurancePoints(endurancePoints);
        setAgilityPoints(agilityPoints);
        setDamagePoints(damagePoints);
        setArmorPoints(armorPoints);
        setCriticalHitPercentage(criticalHitPercentage);
    }
}
