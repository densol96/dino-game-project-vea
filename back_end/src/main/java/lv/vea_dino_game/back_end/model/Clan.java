package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lv.vea_dino_game.back_end.model.enums.DinoType;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "clans")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Clan {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title cannot be blank/null")
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank/null")
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    private String description;

    @Min(value = 0, message = "Maximum capacity can not be negative number")
    @Max(value = 100, message = "Maximum capacity can not be greater than 100")
    @NotNull(message = "Max capacity cannot be null")
    private Integer maxCapacity;

    @Min(value = 0, message = "Minimum player level can not be negative number")
    @Max(value = 100, message = "Minimum player level can not be greater than 100")
    @NotNull(message = "Min player level cannot be null")
    private Integer minPlayerLevel;

    @OneToMany(mappedBy = "clan")
    @ToString.Exclude
    //@JsonManagedReference
    public List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy = "clan")
    //@JsonManagedReference
    private List<Announcement> announcements;

    @OneToOne
    @JoinColumn(name="admin")
    private Player admin;

    @NotNull(message = "Dino type cannot be null")
    @Enumerated(EnumType.STRING)
    private DinoType dinoType;

    public void setSinglePlayer(Player player) {
        this.players.clear();
        this.players.add(player);
        player.setClan(this);
    }



    public Clan(String title, String description, int maxCapacity, int minPlayerLevel){
        setTitle(title);
        setDescription(description);
        setMaxCapacity(maxCapacity);
        setMinPlayerLevel(minPlayerLevel);
    }


}
