package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity

@Table(name = "PlayerTable")
public class Player extends User{
    @ManyToOne
    @JoinColumn(name = "Idc")
    private Clan clan;

    @OneToOne
    @JoinColumn(name = "IdIn")
    private InGameStats inGameStats;

    @Column(name = "DinoType")
    private DinoType dinoType;

    @Column(name = "Level")
    @Min(0)
    @Max(15)
    private int level;

    public Player(String username, String password, Clan clan, InGameStats inGameStats){
        super(username, password);
        setClan(clan);
        setInGameStats(inGameStats);
    }


}
