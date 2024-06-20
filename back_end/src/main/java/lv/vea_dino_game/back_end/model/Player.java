package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.DinoType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "players")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Player {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    private Clan clan;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_stats")
    private PlayerStats playerStats;

    @OneToOne(cascade = CascadeType.ALL)
    private Job currentJob = null;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_combat_stats")
    private PlayerCombatsStats combatStats = null;

    @NotNull(message = "Dino type cannot be null")
    @Enumerated(EnumType.STRING)
    private DinoType dinoType;

    @Min(value = 0, message = "xp can not be less than 0")
    private Integer experience = 0;

    @Min(value = 0, message = "currency can not be less than 0")
    private Integer currency = 0;

    @Min(value = 1, message = "Level can not be less than 1")
    @Max(value = 10, message = "Level can not be greater than 10")
    private Integer level = 1;

    private LocalDateTime immuneUntil = LocalDateTime.now(); // set immuneUntil to 24 hours from now (now removed for testing purposes)

    private LocalDateTime workingUntil = LocalDateTime.now();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    private List<Announcement> announcement;

    public Player(Clan clan, PlayerStats playerStats, DinoType dinoType) {
        setDinoType(dinoType);
        setClan(clan);
        setPlayerStats(playerStats);
        setCombatStats(new PlayerCombatsStats());
    }


}
