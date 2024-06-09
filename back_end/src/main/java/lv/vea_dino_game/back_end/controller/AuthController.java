package lv.vea_dino_game.back_end.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;
import lv.vea_dino_game.back_end.service.impl.AuthServiceImpl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;
  
  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignUpDto signUpData) {
    return new ResponseEntity<AuthResponse>(authService.signUp(signUpData), HttpStatus.OK);
  }
  
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody SignInDto signInCredentials) {
    return new ResponseEntity<AuthResponse>(authService.signIn(signInCredentials), HttpStatus.OK);
  }
  
}
