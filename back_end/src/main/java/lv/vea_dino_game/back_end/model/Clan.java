package lv.vea_dino_game.back_end.model;

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
    @Size(min = 4, max = 50)
    private String title;

    @NotBlank
    @Size(min = 4, max = 50)
    private String description;

    @Min(0)
    @Max(100)
    private Integer maxCapacity;

    @Min(0)
    @Max(100)
    private Integer minPlayerLevel;

    @OneToMany(mappedBy = "clan")
    @ToString.Exclude
    public List<Player> players;

    public Clan(String title, String description, int maxCapacity, int minPlayerLevel){
        setTitle(title);
        setDescription(description);
        setMaxCapacity(maxCapacity);
        setMinPlayerLevel(minPlayerLevel);
    }
}
