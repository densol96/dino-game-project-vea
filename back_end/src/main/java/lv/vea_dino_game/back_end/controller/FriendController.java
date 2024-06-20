package lv.vea_dino_game.back_end.controller;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.AllAnnouncementDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.FriendDto;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IFriendService;
import lv.vea_dino_game.back_end.service.IPlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/friends")
@RequiredArgsConstructor
public class FriendController {

    private final IFriendService friendService;

    @PostMapping("/request/{friendId}")
    public ResponseEntity<BasicMessageResponse> sendInvitationToBeFriend(@PathVariable("friendId") Integer friendId){
        return new ResponseEntity<BasicMessageResponse>(friendService.sendInvitationToBeFriend(friendId), HttpStatus.CREATED);
    }

    @PostMapping("/accept/{friendId}")
    public ResponseEntity<BasicMessageResponse> acceptFriendRequest(@PathVariable("friendId") Integer friendId){
        return new ResponseEntity<BasicMessageResponse>(friendService.acceptFriendRequest(friendId), HttpStatus.CREATED);
    }

    @PostMapping("/reject/{friendId}")
    public ResponseEntity<BasicMessageResponse> rejectFriendRequest(@PathVariable("friendId") Integer friendId){
        return new ResponseEntity<BasicMessageResponse>(friendService.rejectFriendRequest(friendId), HttpStatus.CREATED);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendDto>> getAnnouncementByFriends(){
        return new ResponseEntity<List<FriendDto>>(friendService.getFriends(), HttpStatus.OK);
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<FriendDto>> getAnnouncementByRejected(){
        return new ResponseEntity<List<FriendDto>>(friendService.getRejected(), HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FriendDto>> getAnnouncementByPending(){
        return new ResponseEntity<List<FriendDto>>(friendService.getPending(), HttpStatus.OK);
    }




}
