package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Combat")
public class Combat {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne
    @JoinColumn(name = "id")
    private CombatResult combatResult;

    @OneToOne
    @JoinColumn(name = "id")
    private CombatHistoryRecap combatHistoryRecap;

    @ManyToOne
    @JoinColumn(name = "id")
    private Player initiator;

    @ManyToOne
    @JoinColumn(name = "Id")
    private Player defender;


    @Min(1)
    @Max(3)
    private int level;

    @Min(10)
    @Max(20)
    private int turnsAmount;

    private LocalDateTime dateTime;

}

