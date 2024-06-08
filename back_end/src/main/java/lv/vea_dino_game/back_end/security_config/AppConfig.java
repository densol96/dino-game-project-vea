package lv.vea_dino_game.back_end.security_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import lv.vea_dino_game.back_end.repos.UserRepository;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
  
  private final UserRepository userRepo;

  @Bean
  public UserDetailsService userDetailsService() {
    // return new UserDetailsService() {
    //   @Override
    //   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     return userRepo.findByUsername(username)
    //                     .orElseThrow(new Supplier<UsernameNotFoundException>() {
    //                       @Override
    //                       public UsernameNotFoundException get() {
    //                           return new UsernameNotFoundException("No user with such username");
    //                       } 
    //                     });
    //   }
    // };

    // Tbh, lambda expressions will make it more readable...
    return username -> userRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("No user with such username"));
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
