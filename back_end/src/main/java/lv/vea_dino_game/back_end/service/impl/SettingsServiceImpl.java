package lv.vea_dino_game.back_end.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidAuthenticationDataException;
import lv.vea_dino_game.back_end.exceptions.UserAlreadyExistsException;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.DescriptionDto;
import lv.vea_dino_game.back_end.model.dto.EmailDto;
import lv.vea_dino_game.back_end.model.dto.PasswordDto;
import lv.vea_dino_game.back_end.model.dto.UsernameDto;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.ISettingsService;

@Service
@RequiredArgsConstructor
public class SettingsServiceImpl implements ISettingsService {

  private final IAuthService authService;
  private final IUserRepo userRepo;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;

  @Override
  public BasicMessageResponse changeDescription(DescriptionDto data) {
      User currentUser = authService.getLoggedInUser();
      currentUser.getPlayer().setDescription(data.description());
      userRepo.save(currentUser);
      return new BasicMessageResponse("Description has been successfully updated");
  }

  @Override
  public BasicMessageResponse changeEmail(EmailDto data) {
    if (userRepo.existsByEmail(data.email())) {
      throw new UserAlreadyExistsException("Email must be unique: user with such email already exists");
    }
    User currentUser = authService.getLoggedInUser();
    currentUser.setEmail(data.email());
    userRepo.save(currentUser);
    return new BasicMessageResponse("Email has been successfully updated");
  }

  @Override
  public BasicMessageResponse changeUsername(UsernameDto data) {
    if (userRepo.existsByUsername(data.username())) {
      throw new UserAlreadyExistsException("Username must be unique: user with such username already exists");
    }
    User currentUser = authService.getLoggedInUser();
    currentUser.setUsername(data.username());
    userRepo.save(currentUser);
    return new BasicMessageResponse("Username has been successfully updated, please log in again using your new username");

  }

  @Override
  public BasicMessageResponse changePassword(PasswordDto data) {
    User currentUser = authService.getLoggedInUser();

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              currentUser.getUsername(),
              data.oldPassword()));
    } catch (BadCredentialsException e) {
      throw new InvalidAuthenticationDataException("Invalid current password");
    }

    currentUser.setPassword(encoder.encode(data.newPassword()));
    userRepo.save(currentUser);
    return new BasicMessageResponse("Password has been successfully updated, please log in again using your new password");
  }
  
}
