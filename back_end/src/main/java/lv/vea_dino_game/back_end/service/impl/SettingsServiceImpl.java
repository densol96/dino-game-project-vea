package lv.vea_dino_game.back_end.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.exceptions.InvalidAuthenticationDataException;
import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.exceptions.UserAlreadyExistsException;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.DescriptionDto;
import lv.vea_dino_game.back_end.model.dto.EmailDto;
import lv.vea_dino_game.back_end.model.dto.PasswordDto;
import lv.vea_dino_game.back_end.model.dto.ReducedPasswordDto;
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

    /*
     * User changing his/her settings through mySettings -> no id passed in from front (and a different controller calling this) 
     * Admin changing other user's settings --> id will be passed and other controller calling this
     */

  @Override
  public BasicMessageResponse changeDescription(DescriptionDto data, Integer id) {
    User currentUser = id == null ? authService.getLoggedInUser()
      : userRepo.findById(id).orElseThrow(() -> new InvalidUserInputException("There is no user with the id of " + id));
    currentUser.getPlayer().setDescription(data.description());
    userRepo.save(currentUser);
    return new BasicMessageResponse("Description has been successfully updated");
  }

  @Override
  public BasicMessageResponse changeEmail(EmailDto data, Integer id) {
    // will also obviously handle the case if user passes the same email for change
    if (userRepo.existsByEmail(data.email())) {
      throw new UserAlreadyExistsException("Email must be unique: user with such email already exists");
    }
    User currentUser = id == null ? authService.getLoggedInUser()
      : userRepo.findById(id).orElseThrow(() -> new InvalidUserInputException("There is no user with the id of " + id));
    currentUser.setEmail(data.email());
    userRepo.save(currentUser);
    return new BasicMessageResponse("Email has been successfully updated");
  }

  @Override
  public BasicMessageResponse changeUsername(UsernameDto data, Integer id) {
    if (userRepo.existsByUsername(data.username())) {
      throw new UserAlreadyExistsException("Username must be unique: user with such username already exists");
    }
    User currentUser = id == null ? authService.getLoggedInUser()
      : userRepo.findById(id).orElseThrow(() -> new InvalidUserInputException("There is no user with the id of " + id));
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
    return new BasicMessageResponse(
        "Password has been successfully updated, please log in again using your new password");
  }
  

  /*
   * The scenario where admin simply changes the password for the user is unlikely since we are going to provide the functionality for user to restore the password via
   * changing the password to a new one with the email-link, but will just keep it here...
   */
  @Override
  public BasicMessageResponse changePassword(ReducedPasswordDto data, Integer id) {
    if(id == null || id < 0)
      throw new InvalidUserInputException("Invalid user id of " + id);
    
    User currentUser = userRepo.findById(id).orElseThrow(() -> new InvalidUserInputException("There is no user with the id of " + id));
    currentUser.setPassword(encoder.encode(data.password()));
    userRepo.save(currentUser);
    return new BasicMessageResponse("Password has been successfully updated.");
  }
  
}
