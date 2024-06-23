package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.EnumCombatResultType;

@Data
@NoArgsConstructor
@Entity
@Table(name = "combat_results")
public class CombatResult {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(mappedBy = "combatResult")
    @JsonIgnore
    public Combat combat;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private Player winner;

    @ManyToOne
    @JoinColumn(name = "loser_id")
    private Player loser;

    @NotNull(message = "Combat result type id cannot be null")
    @Enumerated(EnumType.STRING)
    private EnumCombatResultType combatResultType;

    @Min(value = 0, message = "Initiator currency change can not be negative number")
    public Integer winnerCurrencyChange = 0;

    public Integer loserCurrencyChange = 0;

    @Min(value = 0, message = "Initiator exp reward can not be negative number")
    public Integer winnerExpReward = 0;

    @Min(value = 0, message = "Defender exp reward can not be negative number")
    public Integer loserExpReward = 0;


}
