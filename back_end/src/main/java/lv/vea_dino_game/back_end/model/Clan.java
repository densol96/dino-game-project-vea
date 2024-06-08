package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "clans")
public class Clan {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    private String title;

    @NotBlank
    @Size(min = 4, max = 50, message = "The title must be minimum 4 characters and maximum 50 characters")
    private String description;

    @Min(value = 0, message = "Maximum capacity can not be negative number")
    @Max(value = 100, message = "Maximum capacity can not be greater than 100")
    private Integer maxCapacity;

    @Min(value = 0, message = "Minimum player level can not be negative number")
    @Max(value = 100, message = "Minimum player level can not be greater than 100")
    private Integer minPlayerLevel;

    @OneToMany(mappedBy = "clan")
    @JsonManagedReference
    @ToString.Exclude
    public List<Player> players;

    public Clan(String title, String description, int maxCapacity, int minPlayerLevel){
        setTitle(title);
        setDescription(description);
        setMaxCapacity(maxCapacity);
        setMinPlayerLevel(minPlayerLevel);
    }
}
