package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.EnumCombatResultType;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "combat_results")
public class CombatResult {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne
    @JoinColumn(name="combat_id")
    @ToString.Exclude
    public Combat combat;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    public EnumCombatResultType combatResultType;

    @Min(value = 0, message = "Initiator currency change can not be negative number")
    public Integer initiatorCurrencyChange = 0;

    @Min(value = 0, message = "Defender currency change can not be negative number")
    public Integer defenderCurrencyChange = 0;

    @Min(value = 0, message = "Initiator exp reward can not be negative number")
    public Integer initiatorExpReward = 0;

    @Min(value = 0, message = "Defender exp reward can not be negative number")
    public Integer defenderExpReward = 0;


}
