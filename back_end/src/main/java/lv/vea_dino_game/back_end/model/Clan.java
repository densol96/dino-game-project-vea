package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity

@Table(name = "ClanTable")
public class Clan {

    @Setter(value = AccessLevel.NONE)
    @Column(name = "Idc")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name = "Title")
    @NotNull
    @Size(min = 4, max = 50)
    public String title;

    @Column(name = "Description")
    @NotNull
    @Size(min = 4, max = 50)
    public String description;

    @Min(0)
    @Max(100)
    @Column(name="MaxCapacity")
    private int maxCapacity;

    @Min(0)
    @Max(100)
    @Column(name="MinPlayerLevel")
    private int minPlayerLevel;

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
