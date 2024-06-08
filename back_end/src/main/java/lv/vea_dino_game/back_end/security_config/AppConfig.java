package lv.vea_dino_game.back_end.security_config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

import lv.vea_dino_game.back_end.repo.IUserRepo;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
  
  private final IUserRepo userRepo;

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
        
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user with such username"));
  }

  /* At first thought lambda can make code less readable but actually it looks much clearer compared to abstract classes:
   * return new UserDetailsService() {
   * @Override
   *  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
   *   return userRepo.findByUsername(username)
   *                   .orElseThrow(new Supplier<UsernameNotFoundException>() {
   *                     @Override
   *                     public UsernameNotFoundException get() {
   *                         return new UsernameNotFoundException("No user with such username");
   *                     } 
   *                   });
   * }
   * };
   */ 
  
}
        