package lv.vea_dino_game.back_end.service.impl;

import lv.vea_dino_game.back_end.service.IAuthService;
import lv.vea_dino_game.back_end.service.IMailService;
import lv.vea_dino_game.back_end.service.helpers.EmailSenderService;
import lv.vea_dino_game.back_end.service.helpers.Mapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.exceptions.InvalidAuthenticationDataException;
import lv.vea_dino_game.back_end.exceptions.InvalidTokenException;
import lv.vea_dino_game.back_end.exceptions.InvalidUserInputException;
import lv.vea_dino_game.back_end.exceptions.NoSuchUserException;
import lv.vea_dino_game.back_end.exceptions.ServiceCurrentlyUnavailableException;
import lv.vea_dino_game.back_end.exceptions.UserAlreadyExistsException;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.BasicMessageResponse;
import lv.vea_dino_game.back_end.model.dto.ResetPasswordDto;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;
import lv.vea_dino_game.back_end.model.dto.UserMainDTO;
import lv.vea_dino_game.back_end.model.enums.Role;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.security_config.JwtService;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
  
  private final IUserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final EmailSenderService emailService;
  private final Mapper mapper;

  @Override
  public BasicMessageResponse signUp(SignUpDto signUpData) {
    if (userRepo.existsByEmail(signUpData.email())) {
      throw new UserAlreadyExistsException("User email must be unique: user with such email already exists");
    } else if (userRepo.existsByUsername(signUpData.username())) {
      throw new UserAlreadyExistsException("Username must be unique: user with such username already exists");
    }
    // Prepare player and Player Stats Objects
    Player player = new Player();
    player.setDinoType(signUpData.dinoType());
    player.setPlayerStats(new PlayerStats());

    // Initialise new User
    User user = new User();
    user.setUsername(signUpData.username());
    user.setPassword(passwordEncoder.encode(signUpData.password()));
    user.setEmail(signUpData.email());
    user.setRole(Role.USER);
    user.setPlayer(player);
    user.setRegistrationDate(LocalDateTime.now());
    /////// FOR TESTING PURPOSES TO DO IT QUICKER IT IS TRUE BY DEFAULT (in prod to be changed back to false as default)
    user.setIsEmailConfirmed(false);
    // In order to pass authentication, apart from valid credentials, email needs to be confirmed via confirmation token
    String confirmationToken = createRandomToken();

    // Hashed token stored in DB
    user.setEmailConfirmationToken(returnHashedToken(confirmationToken));
  
    // Send the email with the token-link to email
    try {
      emailService.sendToAskToConfirmEmail(user, confirmationToken); 
      userRepo.save(user);
    } catch (Exception e) {
      System.out.println("💥💥💥 Likely error sending email ---> " + e.getMessage());
      throw new ServiceCurrentlyUnavailableException(
          "Sign up is temporarily unavailable, please try again a bit later. If the problem persists, get in touch with the administrator of DinoConflict.");
    }
    return new BasicMessageResponse("User has been successfully created! Please check your email for activation link!");
  }
  
  @Override
  public AuthResponse signIn(SignInDto signInCredentials) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              signInCredentials.username(),
              signInCredentials.password()));
    } catch (BadCredentialsException e) {
      throw new InvalidAuthenticationDataException("Invalid username or password");
    } catch (DisabledException e) {
      throw new InvalidAuthenticationDataException(
          "You have not confirmed your identity yet. Pleas check your email for confirmation letter.");
    } catch (LockedException e) {
        Optional<User> userOptional = userRepo.findByUsername(signInCredentials.username());
        if (userOptional.isPresent()) {
          User user = userOptional.get();
          String message = "Your account has been locked ";
          if (user.getAccountDisabled()) {
            message += "permanently. ";
          } else {
            LocalDateTime ban = user.getTempBanDateTime();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String formattedDate = ban.format(formatter);
            message += "until " + formattedDate + ". ";
          }
          throw new InvalidAuthenticationDataException(message + "If you think this is a mistake, get in touch with the admin.");
        } else {
          throw new InvalidAuthenticationDataException("User not found");
        }
      }

      Optional<User> userOptional = userRepo.findByUsername(signInCredentials.username());
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        user.setLastLoggedIn(LocalDateTime.now());
        userRepo.save(user);
        return new AuthResponse(jwtService.generateToken(user));
      } else {
        throw new InvalidAuthenticationDataException("User not found");
      }
  }

  @Override
  public BasicMessageResponse confirmEmail(String confirmationToken) {
    InvalidTokenException e = new InvalidTokenException(
        "Invalid confirmation token. If you are sure this is a mistake, please let the administrator know.");

    // Since we are using the 32 byte token via base hex for actual representation in a string, it will be 64 chars exactly in a string
    if (confirmationToken == null || confirmationToken.length() != 64)
      throw e;

    String hashedToken = returnHashedToken(confirmationToken);
    User user = userRepo.findByEmailConfirmationToken(hashedToken).orElseThrow(() -> e);
    user.setEmailConfirmationToken(null);
    user.setIsEmailConfirmed(true);
    userRepo.save(user);
    return new BasicMessageResponse("Your email has been confirmed! You can now try to log in!");
  }

  @Override
  public User getLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof UserDetails))
      throw new ServiceCurrentlyUnavailableException("Service 'get-me' is temporarily not working");
    return (User) auth.getPrincipal();
  }

  @Override
  public UserMainDTO getMe() {
    return mapper.fromUserToDto(getLoggedInUser());
  }

  @Override
  public BasicMessageResponse forgotPassword(String email) {
    if (email == null) {
      throw new InvalidUserInputException("Invalid user email input");
    }
    User user = userRepo.findByEmail(email)
        .orElseThrow(() -> new NoSuchUserException("We are are unaware of such email for any user"));
    String passwordResetToken = createRandomToken();
    user.setPasswordResetToken(returnHashedToken(passwordResetToken));
    try {
      emailService.sendPasswordResetToken(user, passwordResetToken);
      userRepo.save(user);
    } catch (Exception e) {
      System.out.println("💥💥💥 Likely error sending email ---> " + e.getMessage());
      throw new ServiceCurrentlyUnavailableException(
          "Password reset is temporarily unavailable, please try again a bit later. If the problem persists, get in touch with the administrator of DinoConflict.");
    }
    return new BasicMessageResponse("Please, check your email and you will find a link to reset your password!");
  }

  @Override
  public BasicMessageResponse resetPassword(String resetToken, ResetPasswordDto dto) {
    if (resetToken == null || !userRepo.existsByPasswordResetToken(returnHashedToken(resetToken))) {
      throw new InvalidUserInputException("Your reset-token is invalid");
    }
    // DTO cannot be null and inpout validated in controller => we are safe here
    User user = userRepo.findByPasswordResetToken(returnHashedToken(resetToken)).orElseThrow(() -> new IllegalArgumentException("Invalid reset token"));
    user.setPasswordResetToken(null);
    user.setPassword(passwordEncoder.encode(dto.password()));
    userRepo.save(user);
    return new BasicMessageResponse("Your password has been changed. You should be able to log in now!");
  }

  // HELPER FUNCTIONS BELOW (could be moved to a separate service ormay be not)
  
  /*
   * Bcrypt is a standard for password encryption that is taking care of salting, encryption, password comparison etc.
   * But in order to implement the password-change and email-confirm functionality we need a different approach.
   * 
   * 1) It can be less-secure and resource-consuming, since we only need to send the token to the user email via link and we expect the user
   * to click on it shorly after sending it away.
   * 
   * 2) We need to process the link and then hash the token again and find the user by this hashed token. => hashing alghorithm should not 
   * use salting and always return the same output for the same input.
   * 
   * Therefore using standard java.security is perfectly fine in this case.
   */

  // this will be included to the click-link and sent to the email and also a hash of this will be stored in the DB
  public String createRandomToken() {

    final int bytesLength = 32;
    byte[] randomBytes = new byte[bytesLength];

    SecureRandom secureRandom = new SecureRandom();
    secureRandom.nextBytes(randomBytes);
    return bytesToHex(randomBytes);
  }

  private String bytesToHex(byte[] byteArray) {
    StringBuilder sb = new StringBuilder();
    for (byte b : byteArray) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  // this will be stored in the DB
  private String hashToken(String token) throws NoSuchAlgorithmException {
    MessageDigest hasher = MessageDigest.getInstance("SHA-256");
    byte[] hashedBytes = hasher.digest(token.getBytes());
    return bytesToHex(hashedBytes);
  }

  private String returnHashedToken(String token) {
    try {
      return hashToken(token);
    } catch (Exception e) {
      throw new ServiceCurrentlyUnavailableException(
          "Sign up is temporarily unavailable, please try again a bit later. If the problem persists, get in touch with the administrator of DinoConflict.");
    }
  }
}
