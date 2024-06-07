package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "CombatResult")
public class CombatResult {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(mappedBy = "id")
    @ToString.Exclude
    public Combat combat;

    @ManyToOne
    @JoinColumn(name = "id")
    private Player winner;

    public EnumCombatResultType combatResultType;

    public Integer initiatorCurrencyChange = 0;

    public Integer defenderCurrencyChange = 0;

    public Integer initiatorExpReward = 0;

    public Integer defenderExpReward = 0;


}
