package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;

public interface IAuthService {
  public AuthResponse signUp(SignUpDto signUpData);
  
  public AuthResponse signIn(SignInDto signInCredentials);
}
