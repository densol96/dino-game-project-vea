package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.dto.AllAnnouncementDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.FriendDto;

import java.util.List;

public interface IFriendService {
    BasicMessageResponse sendInvitationToBeFriend(Integer friendId);

    BasicMessageResponse acceptFriendRequest(Integer friendId);

    BasicMessageResponse rejectFriendRequest(Integer friendId);

    List<FriendDto> getFriends();

    List<FriendDto> getRejected();

    List<FriendDto> getPending();
}
