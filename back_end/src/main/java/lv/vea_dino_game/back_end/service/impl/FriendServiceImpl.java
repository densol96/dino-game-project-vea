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
import java.util.Optional;
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
        if (me == null) throw new InvalidPlayerException("Invalid you");

        Optional<Player> friendOptional = playerRepo.findById(friendId);
        if (friendOptional.isEmpty()) {
            throw new InvalidPlayerException("Invalid friend");
        }
        Player friend = friendOptional.get();
        if (friend.equals(me)) {
            return new BasicMessageResponse("You cannot befriend yourself");
        }

        Friend existingFriendship = friendRepo.findByPlayerAndFriendOrFriendAndPlayer(me, friend);
        if (existingFriendship != null) {
            if (existingFriendship.getStatus() == FriendStatus.REJECTED) {
                existingFriendship.setStatus(FriendStatus.PENDING);
                friendRepo.save(existingFriendship);
                return new BasicMessageResponse("Friend request to user " + friend.getUser().getUsername() + " has been sent again!");
            } else {
                throw new FriendshipException("Friendship with user " + friend.getUser().getUsername() + " already exists");
            }
        }

        Friend request = new Friend(me, friend, FriendStatus.PENDING);
        friendRepo.save(request);
        return new BasicMessageResponse("Friend request to user " + friend.getUser().getUsername() + " has been sent!");
    }

    @Override
    public BasicMessageResponse acceptFriendRequest(Integer friendId) {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null) throw new InvalidPlayerException("Invalid you");

        Optional<Player> friendOptional = playerRepo.findById(friendId);
        if (friendOptional.isEmpty()) {
            throw new InvalidPlayerException("Invalid friend");
        }
        Player friend = friendOptional.get();

        Friend friendship = friendRepo.findByPlayerAndFriendOrFriendAndPlayer(friend, me);
        if (friendship == null) {
            throw new InvalidPlayerException("Friendship request does not exist");
        }

        if (friendship.getStatus() == FriendStatus.PENDING) {
            friendship.setStatus(FriendStatus.ACCEPTED);
            friendRepo.save(friendship);
            return new BasicMessageResponse("Friendship with user " + friend.getUser().getUsername() + " has been accepted!");
        }

        throw new FriendshipException("Friendship with user " + friend.getUser().getUsername() + " already exists");
    }

    @Override
    public BasicMessageResponse rejectFriendRequest(Integer friendId) {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null) throw new InvalidPlayerException("Invalid you");

        Optional<Player> friendOptional = playerRepo.findById(friendId);
        if (friendOptional.isEmpty()) {
            throw new InvalidPlayerException("Invalid friend");
        }
        Player friend = friendOptional.get();

        Friend friendship = friendRepo.findByPlayerAndFriendOrFriendAndPlayer(friend, me);
        if (friendship == null) {
            throw new InvalidPlayerException("Friendship request does not exist");
        }

        if (friendship.getStatus() == FriendStatus.PENDING) {
            friendship.setStatus(FriendStatus.REJECTED);
            friendRepo.save(friendship);
            return new BasicMessageResponse("Friendship with user " + friend.getUser().getUsername() + " has been rejected!");
        }

        throw new FriendshipException("Friendship with user " + friend.getUser().getUsername() + " already exists");
    }

    @Override
    public List<FriendDto> getFriends() {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null) throw new InvalidPlayerException("Invalid you");

        List<Friend> friendships = friendRepo.findAllByStatusAndPlayerOrFriend(FriendStatus.ACCEPTED, me);
        if (friendships.isEmpty()) {
            throw new InvalidPlayerException("No accepted friends");
        }

        return friendships.stream().map(mapper::oneFriendToDto).collect(Collectors.toList());
    }

    @Override
    public List<FriendDto> getRejected() {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null) throw new InvalidPlayerException("Invalid you");

        List<Friend> friendships = friendRepo.findAllByStatusAndPlayerOrFriend(FriendStatus.REJECTED, me);
        if (friendships.isEmpty()) {
            throw new InvalidPlayerException("No rejected friends");
        }

        return friendships.stream().map(mapper::oneFriendToDto).collect(Collectors.toList());
    }

    @Override
    public List<FriendDto> getPending() {
        UserMainDTO user = authService.getMe();
        Player me = playerRepo.findByUserId(user.id());
        if (me == null) throw new InvalidPlayerException("Invalid you");

        List<Friend> friendships = friendRepo.findAllByStatusAndPlayerOrFriend(FriendStatus.PENDING, me);
        if (friendships.isEmpty()) {
            throw new InvalidPlayerException("No pending friendships");
        }

        return friendships.stream().map(mapper::oneFriendToDto).collect(Collectors.toList());
    }


}

