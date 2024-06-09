package lv.vea_dino_game.back_end.service.impl;

import lv.vea_dino_game.back_end.service.IAuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.exceptions.InvalidAuthenticationDataException;
import lv.vea_dino_game.back_end.exceptions.UserAlreadyExistsException;
import lv.vea_dino_game.back_end.model.Player;
import lv.vea_dino_game.back_end.model.PlayerStats;
import lv.vea_dino_game.back_end.model.User;
import lv.vea_dino_game.back_end.model.dto.AuthResponse;
import lv.vea_dino_game.back_end.model.dto.SignInDto;
import lv.vea_dino_game.back_end.model.dto.SignUpDto;
import lv.vea_dino_game.back_end.model.enums.Role;
import lv.vea_dino_game.back_end.repo.IUserRepo;
import lv.vea_dino_game.back_end.security_config.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
  
  private final IUserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthResponse signUp(SignUpDto signUpData) {
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

    // Cascading will do the rest
    userRepo.save(user);

    return new AuthResponse(jwtService.generateToken(user));
  }
  
  @Override
  public AuthResponse signIn(SignInDto signInCredentials) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              signInCredentials.username(),
              signInCredentials.password()));
    } catch (Exception e) {
      throw new InvalidAuthenticationDataException("Invalid username or password");
    }
    User user = userRepo.findByUsername(signInCredentials.username()).get();
    return new AuthResponse(jwtService.generateToken(user));
  }
  
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
  public String hashToken(String token) throws NoSuchAlgorithmException {
    MessageDigest hasher = MessageDigest.getInstance("SHA-256");
    byte[] hashedBytes = hasher.digest(token.getBytes());
    return bytesToHex(hashedBytes);
  }
}
