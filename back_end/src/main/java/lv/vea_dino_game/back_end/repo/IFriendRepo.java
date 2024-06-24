package lv.vea_dino_game.back_end.repo;

import lv.vea_dino_game.back_end.model.Friend;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.enums.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IFriendRepo extends JpaRepository<Friend, Integer> {


    @Query("SELECT f FROM Friend f WHERE (f.player = :player AND f.friend = :friend) OR (f.player = :friend AND f.friend = :player)")
    Friend findByPlayerAndFriendOrFriendAndPlayer(Player player, Player friend);

    @Query("SELECT f FROM Friend f WHERE f.status = :status AND (f.player = :player OR f.friend = :player)")
    List<Friend> findAllByStatusAndPlayerOrFriend(FriendStatus status, Player player);


}
