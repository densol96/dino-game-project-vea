package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BanDto;
import lv.vea_dino_game.back_end.model.dto.BanWithTimeDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/ban-without-time/{id}")
    public ResponseEntity<BasicMessageResponse> banUserById(@PathVariable("id") Integer id, @Valid BanDto info){
        return new ResponseEntity<>(userService.banUser(id, info), HttpStatus.CREATED);
    }

    @PostMapping("/unban/{id}")
    public ResponseEntity<BasicMessageResponse> unbanUserById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.unbanUser(id), HttpStatus.CREATED);
    }

    @PostMapping("/ban-with-time/{id}")
    public ResponseEntity<BasicMessageResponse> banUserByIdWithTime(@PathVariable("id") Integer id, @Valid BanWithTimeDto info){
        return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.CREATED);
    }



}
