package lv.vea_dino_game.back_end.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity

@Table(name = "PlayerTable")
public class Player extends User{

    @Column(name = "Nickname")
    @NotNull
    @Size(min = 2, max = 30)
    private String nickname;

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

    public Player(String username, String password,String nickname, Clan clan, InGameStats inGameStats, DinoType dinoType){
        super(username, password);
        setNickname(nickname);
        setDinoType(dinoType);
        setClan(clan);
        setInGameStats(inGameStats);
    }


}
