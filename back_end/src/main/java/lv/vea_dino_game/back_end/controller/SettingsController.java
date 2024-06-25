package lv.vea_dino_game.back_end.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.DescriptionDto;
import lv.vea_dino_game.back_end.model.dto.EmailDto;
import lv.vea_dino_game.back_end.model.dto.PasswordDto;
import lv.vea_dino_game.back_end.model.dto.UsernameDto;
import lv.vea_dino_game.back_end.service.impl.SettingsServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/settings")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
@RequiredArgsConstructor
public class SettingsController {

  public final SettingsServiceImpl settingsService;

  @PostMapping("/description")
  public ResponseEntity<BasicMessageResponse> changeDescription(@Valid @RequestBody DescriptionDto data) {
    return new ResponseEntity<>(settingsService.changeDescription(data, null), HttpStatus.OK);
  }
  
  @PostMapping("/email")
  public ResponseEntity<BasicMessageResponse> changeEmail(@Valid @RequestBody EmailDto data) {
    return new ResponseEntity<>(settingsService.changeEmail(data, null), HttpStatus.OK);
  }
  
  @PostMapping("/username")
  public ResponseEntity<BasicMessageResponse> changeUsername(@Valid @RequestBody UsernameDto data ) {
    return new ResponseEntity<>(settingsService.changeUsername(data, null), HttpStatus.OK);
  }
  
  @PostMapping("/password")
  public ResponseEntity<BasicMessageResponse> changePassword(@Valid @RequestBody PasswordDto data) {
    return new ResponseEntity<>(settingsService.changePassword(data), HttpStatus.OK);
  }
}
