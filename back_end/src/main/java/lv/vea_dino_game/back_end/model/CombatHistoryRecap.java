package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "combat_history_recaps")
public class CombatHistoryRecap {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @OneToOne(mappedBy = "combatHistoryRecap")
    @JsonIgnore
    public Combat combat;
}
