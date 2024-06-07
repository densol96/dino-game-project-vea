package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.DinoType;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    private Clan clan;

    @OneToOne
    @JoinColumn(name = "player_stats")
    private PlayerStats playerStats;

    @NotNull
    private DinoType dinoType;

    @Min(value = 1, message = "Level can not be less than 1")
    @Max(value = 15, message = "Level can not be greater than 15")
    private Integer level;

    public Player(Clan clan, PlayerStats playerStats, DinoType dinoType) {
        setDinoType(dinoType);
        setClan(clan);
        setPlayerStats(playerStats);
    }


}
