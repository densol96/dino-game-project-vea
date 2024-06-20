package lv.vea_dino_game.back_end.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lv.vea_dino_game.back_end.model.enums.FriendStatus;

@Data
@NoArgsConstructor
@Entity
@Table(name = "friends")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Friend {

    @Setter(value = AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "player")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "friend")
    private Player friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    public Friend(Player player, Player friend, FriendStatus status) {
        this.player = player;
        this.friend = friend;
        this.status = status;
    }




}
