package lv.vea_dino_game.back_end.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.ResetPasswordDto;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.service.impl.AuthServiceImpl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"})
@RequiredArgsConstructor
public class AuthController {

  private final AuthServiceImpl authService;
  
  @PostMapping("/register")
  public ResponseEntity<BasicMessageResponse> register(@Valid @RequestBody SignUpDto signUpData) {
    return new ResponseEntity<>(authService.signUp(signUpData), HttpStatus.CREATED);
  }
  
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody SignInDto signInCredentials) {
    return new ResponseEntity<>(authService.signIn(signInCredentials), HttpStatus.OK);
  }

  @GetMapping("/email-confirmation/{confirmationToken}")
  public ResponseEntity<BasicMessageResponse> confirmEmail(@PathVariable String confirmationToken) {
    return new ResponseEntity<>(authService.confirmEmail(confirmationToken), HttpStatus.OK);
  }

  @GetMapping("/forgot-password/{email}")
  public ResponseEntity<BasicMessageResponse> resetPassword(@PathVariable String email) {
    return new ResponseEntity<>(authService.forgotPassword(email), HttpStatus.OK);
  }

  @PostMapping("/password-reset/{resetToken}")
  public ResponseEntity<BasicMessageResponse> resetPassword(@PathVariable("resetToken") String resetToken, @Valid @RequestBody ResetPasswordDto dto) {
    return new ResponseEntity<>(authService.resetPassword(resetToken, dto), HttpStatus.OK);
  }

  @GetMapping("/me")
  public UserMainDTO getMethodName() {
    return authService.getMe();
  }
  
  
  
}
