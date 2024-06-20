package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Friend;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFriendRepo extends JpaRepository<Friend, Integer> {
    Friend findByPlayerAndFriend(Player me, Player friend);

    Friend findByPlayerAndFriendOrFriendAndPlayer(Player me, Player friend, Player friend1, Player me1);

    List<Friend> findAllByPlayerOrFriendAndStatus(Player me, Player me1, FriendStatus friendStatus);

    List<Friend> findAllByStatusAndPlayerOrFriend(FriendStatus friendStatus, Player me, Player me1);
}
