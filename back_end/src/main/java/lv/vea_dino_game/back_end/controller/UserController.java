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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final IUserService userService;

    @PostMapping("/ban-without-time/{id}")
    public ResponseEntity<BasicMessageResponse> banUserById(@PathVariable("id") Integer id, @Valid BanDto info){
        return new ResponseEntity<>(userService.banUser(id, info), HttpStatus.OK);
    }

    @PostMapping("/unban/{id}")
    public ResponseEntity<BasicMessageResponse> unbanUserById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.unbanUser(id), HttpStatus.OK);
    }

    @PostMapping("/ban-with-time/{id}")
    public ResponseEntity<BasicMessageResponse> banUserByIdWithTime(@PathVariable("id") Integer id,
        @Valid @RequestBody BanWithTimeDto info) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }
    
    @GetMapping("/id-by/{username}")
    public ResponseEntity<Integer> getUserIdByUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.getUserIdByUsername(username), HttpStatus.OK);
    }



}