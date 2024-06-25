package lv.vea_dino_game.back_end.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.BooleanDto;
import lv.vea_dino_game.back_end.model.dto.DateTimeDto;
import lv.vea_dino_game.back_end.model.dto.DescriptionDto;
import lv.vea_dino_game.back_end.model.dto.EmailDto;
import lv.vea_dino_game.back_end.model.dto.ManageUserDto;
import lv.vea_dino_game.back_end.model.dto.ReducedPasswordDto;
import lv.vea_dino_game.back_end.model.dto.UsernameDto;
import lv.vea_dino_game.back_end.service.ISettingsService;
import lv.vea_dino_game.back_end.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
public class UserController {

    private final IUserService userService;
    private final ISettingsService settingsService;
    
    @GetMapping("/id-by/{username}")
    public ResponseEntity<Integer> getUserIdByUsername(@PathVariable String username) {
      return new ResponseEntity<>(userService.getUserIdByUsername(username), HttpStatus.OK);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/detailed-user-info/{id}")
    public ResponseEntity<ManageUserDto> getMethodName(@PathVariable Integer id) {
      return new ResponseEntity<>(userService.getDetailedUserInfo(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change/accountDisabled/{id}")
    public ResponseEntity<BasicMessageResponse> changedDisabledStatus(@PathVariable("id") Integer id, @Valid @RequestBody BooleanDto info){
        return new ResponseEntity<>(userService.changeDisabledStatus(id, info), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change/tempBanDateTime/{id}")
    public ResponseEntity<BasicMessageResponse> banUserByIdWithTime(@PathVariable("id") Integer id,
        @Valid @RequestBody DateTimeDto info) {
      return new ResponseEntity<>(userService.giveTempBan(id, info), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change/description/{id}")
    public ResponseEntity<BasicMessageResponse> changeDescription(@PathVariable("id") Integer id,
        @Valid @RequestBody DescriptionDto data) {
      return new ResponseEntity<>(settingsService.changeDescription(data, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change/email/{id}")
    public ResponseEntity<BasicMessageResponse> changeEmail(@PathVariable("id") Integer id,
        @Valid @RequestBody EmailDto data) {
      return new ResponseEntity<>(settingsService.changeEmail(data, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change/username/{id}")
    public ResponseEntity<BasicMessageResponse> changeUsername(@PathVariable("id") Integer id,
        @Valid @RequestBody UsernameDto data) {
      return new ResponseEntity<>(settingsService.changeUsername(data, id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/change/password/{id}")
    public ResponseEntity<BasicMessageResponse> changePassword(@PathVariable("id") Integer id,
        @Valid @RequestBody ReducedPasswordDto data) {
      return new ResponseEntity<>(settingsService.changePassword(data, id), HttpStatus.OK);
    }
}
