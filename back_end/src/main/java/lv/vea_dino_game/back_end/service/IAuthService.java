package lv.vea_dino_game.back_end.service;

import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.ResetPasswordDto;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;

public interface IAuthService {
  BasicMessageResponse signUp(SignUpDto signUpData);
  
  AuthResponse signIn(SignInDto signInCredentials);

  BasicMessageResponse confirmEmail(String confirmationToken);

  UserMainDTO getMe();

  User getLoggedInUser();

  BasicMessageResponse forgotPassword(String email);

  BasicMessageResponse resetPassword(String resetToken, ResetPasswordDto dto);


}
