package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "combats")
public class Combat {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(mappedBy="combat")
    private CombatResult combatResult;

    @OneToOne(mappedBy = "combat")
    private CombatHistoryRecap combatHistoryRecap;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    @NotNull(message = "Initiator cannot be null")
    private Player initiator;

    @ManyToOne
    @JoinColumn(name = "defender_id")
    @NotNull(message = "Defender cannot be null")
    private Player defender;


    @Min(value = 1, message = "Minimum player level can not be less than 1")
    @Max(value = 3, message = "Maximum player level can not be greater than 3")
    private Integer level;

    @Min(value = 10, message = "Turns amount value can not be less than 10")
    @Max(value = 20, message = "Turns amount value can not greater than 20")
    private Integer turnsAmount;

    private LocalDateTime dateTime;

}

