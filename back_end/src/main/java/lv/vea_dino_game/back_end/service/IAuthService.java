package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;

public interface IAuthService {
  AuthResponse signUp(SignUpDto signUpData);
  
  AuthResponse signIn(SignInDto signInCredentials);
}
