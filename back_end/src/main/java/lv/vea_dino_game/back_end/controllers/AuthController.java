package lv.vea_dino_game.back_end.controllers;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;
import lv.vea_dino_game.back_end.service.AuthService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignUpDto signUpData) {
    return new ResponseEntity<AuthResponse>(authService.signUp(signUpData), HttpStatus.OK);
  }
  
  @PostMapping("/authenticate")
  public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody SignInDto signInCredentials) {
      return new ResponseEntity<AuthResponse>(authService.signIn(signInCredentials), HttpStatus.OK);
  }
  
}
