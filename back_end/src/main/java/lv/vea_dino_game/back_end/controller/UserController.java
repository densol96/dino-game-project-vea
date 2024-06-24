package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BanDto;
import lv.vea_dino_game.back_end.model.dto.BanWithTimeDto;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.BooleanDto;
import lv.vea_dino_game.back_end.model.dto.DescriptionDto;
import lv.vea_dino_game.back_end.model.dto.EmailDto;
import lv.vea_dino_game.back_end.model.dto.ManageUserDto;
import lv.vea_dino_game.back_end.model.dto.ReducedPasswordDto;
import lv.vea_dino_game.back_end.model.dto.UsernameDto;
import lv.vea_dino_game.back_end.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final IUserService userService;
    
    @GetMapping("/id-by/{username}")
    public ResponseEntity<Integer> getUserIdByUsername(@PathVariable String username) {
      return new ResponseEntity<>(userService.getUserIdByUsername(username), HttpStatus.OK);
    }
    
    @GetMapping("/detailed-user-info/{id}")
    public ResponseEntity<ManageUserDto> getMethodName(@PathVariable Integer id) {
      return new ResponseEntity<>(userService.getDetailedUserInfo(id), HttpStatus.OK);
    }

    @PatchMapping("/ban-without-time/{id}")
    public ResponseEntity<BasicMessageResponse> banUserById(@PathVariable("id") Integer id, @Valid BanDto info){
        return new ResponseEntity<>(userService.banUser(id, info), HttpStatus.OK);
    }

    @PatchMapping("/unban/{id}")
    public ResponseEntity<BasicMessageResponse> unbanUserById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.unbanUser(id), HttpStatus.OK);
    }

    @PatchMapping("/tempBanDateTime/{id}")
    public ResponseEntity<BasicMessageResponse> banUserByIdWithTime(@PathVariable("id") Integer id,
        @Valid @RequestBody BanWithTimeDto info) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }

    @PatchMapping("/description/{id}")
    public ResponseEntity<BasicMessageResponse> changeDescription(@PathVariable("id") Integer id,
        @Valid @RequestBody DescriptionDto data) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }

    @PatchMapping("/email/{id}")
    public ResponseEntity<BasicMessageResponse> changeEmail(@PathVariable("id") Integer id,
        @Valid @RequestBody EmailDto data) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }

    @PatchMapping("/username/{id}")
    public ResponseEntity<BasicMessageResponse> changeUsername(@PathVariable("id") Integer id,
        @Valid @RequestBody UsernameDto data) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<BasicMessageResponse> changePassword(@PathVariable("id") Integer id,
        @Valid @RequestBody ReducedPasswordDto data) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }

    @PatchMapping("/accountDisabled/{id}")
    public ResponseEntity<BasicMessageResponse> changeAccountDisabled(@PathVariable("id") Integer id,
        @Valid @RequestBody BooleanDto data) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }

    @PatchMapping("/isEmailConfirmed/{id}")
    public ResponseEntity<BasicMessageResponse> changeIsEmailConfirmed(@PathVariable("id") Integer id,
        @Valid @RequestBody BooleanDto data) {
      return new ResponseEntity<>(userService.banUserWithTime(id, info), HttpStatus.OK);
    }
    
    
    
    
    



}
