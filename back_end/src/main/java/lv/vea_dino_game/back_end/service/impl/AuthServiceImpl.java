package lv.vea_dino_game.back_end.service.impl;

import lv.vea_dino_game.back_end.service.IAuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
          signInCredentials.password()
        )
      );
    } catch (Exception e) {
      throw new InvalidAuthenticationDataException("Invalid username or password");
    }
    User user = userRepo.findByUsername(signInCredentials.username()).get();
    return new AuthResponse(jwtService.generateToken(user));
  }
}
