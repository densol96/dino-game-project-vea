package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
@Table(name = "player_combat_stats")
public class PlayerCombatsStats {
    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(mappedBy = "combatStats")
    @JsonIgnore
    public Player player;

    @Min(value = 0, message = "combatsTotal value can not be less than 0")
    private Integer combatsTotal = 0;

    @Min(value = 0, message = "combatsWon value can not be less than 0")
    private Integer combatsWon = 0;

    private Integer currencyWon = 0;

    private Integer currencyLost = 0;
}
