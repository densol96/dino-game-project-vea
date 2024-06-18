package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.DinoType;

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

    @NotNull(message = "Dino type cannot be null")
    @Enumerated(EnumType.STRING)
    private DinoType dinoType;

    @Min(value = 1, message = "Level can not be less than 1")
    @Max(value = 3, message = "Level can not be greater than 3")
    private Integer level = 1;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    private List<Announcement> announcement;

    public Player(Clan clan, PlayerStats playerStats, DinoType dinoType) {
        setDinoType(dinoType);
        setClan(clan);
        setPlayerStats(playerStats);
    }


}
