package lv.vea_dino_game.back_end.service.impl;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidPlayerException;
import lv.vea_dino_game.back_end.model.Friend;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.FriendDto;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.model.enums.FriendStatus;
import lv.vea_dino_game.back_end.repo.IFriendRepo;
import lv.vea_dino_game.back_end.repo.IPlayerRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IFriendService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements IFriendService {

    private final IFriendRepo friendRepo;

    private final IAuthService authService;

    private final IPlayerRepo playerRepo;

    private final Mapper mapper;


    @Override
    public BasicMessageResponse sendInvitationToBeFriend(Integer friendId) {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null)
            throw new InvalidPlayerException("Invalid you");
        Player friend = playerRepo.findById(friendId).get();
        if (friend == null) {
            throw new InvalidPlayerException("Invalid friend");
        }
        Friend friendship = friendRepo.findByPlayerAndFriendOrFriendAndPlayer(me,friend,friend,me);
        if (friendship != null) {
            if (friendship.getStatus() == FriendStatus.REJECTED){
                friendship.setStatus(FriendStatus.PENDING);
                friendRepo.save(friendship);
                return new BasicMessageResponse("Friend request to user "+friend.getUser().getUsername()+" has been sent again!");
            }
            throw new InvalidPlayerException("Friendship with user "+friend.getUser().getUsername()+"  already exists"); // make new Exception that is connected with friend
        }
        Friend request = new Friend(me, friend, FriendStatus.PENDING);
        friendRepo.save(request);
        return new BasicMessageResponse("Friend request to user "+friend.getUser().getUsername()+" has been sent!");
    }

    @Override
    public BasicMessageResponse acceptFriendRequest(Integer friendId) {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null)
            throw new InvalidPlayerException("Invalid you");
        Player friend = playerRepo.findById(friendId).get();
        if (friend == null) {
            throw new InvalidPlayerException("Invalid friend");
        }
        Friend friendship = friendRepo.findByPlayerAndFriendOrFriendAndPlayer(me,friend,friend,me);
        if (friendship == null) {
            throw new InvalidPlayerException("Friendship request does not exist");
        }
        if (friendship.getStatus() == FriendStatus.PENDING) {
            friendship.setStatus(FriendStatus.ACCEPTED);
            friendRepo.save(friendship);
            return new BasicMessageResponse("Friendship with user "+ friend.getUser().getUsername()+" has been accepted!");
        }
        if (friendship.getStatus() == FriendStatus.ACCEPTED){
            throw new InvalidPlayerException("Friendship request with user "+friend.getUser().getUsername()+" already exists");
        }
        throw new InvalidPlayerException("Friendship request does not exist"); // Exception about friends
    }

    @Override
    public BasicMessageResponse rejectFriendRequest(Integer friendId) {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null)
            throw new InvalidPlayerException("Invalid you");
        Player friend = playerRepo.findById(friendId).get();
        if (friend == null) {
            throw new InvalidPlayerException("Invalid friend");
        }
        Friend friendship = friendRepo.findByPlayerAndFriendOrFriendAndPlayer(me,friend,friend,me);
        if (friendship == null) {
            throw new InvalidPlayerException("Friendship request does not exist");
        }
        if (friendship.getStatus() == FriendStatus.PENDING) {
            friendship.setStatus(FriendStatus.REJECTED);
            friendRepo.save(friendship);
            return new BasicMessageResponse("Friendship with user "+ friend.getUser().getUsername()+" has been rejected!");
        }
        if (friendship.getStatus() == FriendStatus.ACCEPTED){
            throw new InvalidPlayerException("Friendship request with user "+friend.getUser().getUsername()+" already exists");
        }
        throw new InvalidPlayerException("Friendship request does not exist");
    }

    @Override
    public List<FriendDto> getFriends() {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null)
            throw new InvalidPlayerException("Invalid you");
        List<Friend> friendship = friendRepo.findAllByStatusAndPlayerOrFriend(FriendStatus.ACCEPTED,me,me);
        if (friendship == null) {
            throw new InvalidPlayerException("No accepted friends");
        }
        return friendship.stream().map(mapper::oneFriendToDto).collect(Collectors.toList());
    }

    @Override
    public List<FriendDto> getRejected() {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null)
            throw new InvalidPlayerException("Invalid you");
        List<Friend> friendship = friendRepo.findAllByStatusAndPlayerOrFriend(FriendStatus.REJECTED,me,me);
        if (friendship == null) {
            throw new InvalidPlayerException("No rejected friends");
        }
        return friendship.stream().map(mapper::oneFriendToDto).collect(Collectors.toList());
    }

    @Override
    public List<FriendDto> getPending() {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null)
            throw new InvalidPlayerException("Invalid you");
        List<Friend> friendship = friendRepo.findAllByStatusAndPlayerOrFriend(FriendStatus.PENDING,me,me);
        if (friendship == null) {
            throw new InvalidPlayerException("No pending friendships");
        }
        return friendship.stream().map(mapper::oneFriendToDto).collect(Collectors.toList());
    }


}

