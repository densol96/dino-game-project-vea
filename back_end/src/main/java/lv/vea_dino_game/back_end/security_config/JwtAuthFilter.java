package lv.vea_dino_game.back_end.security_config;

import java.io.IOException;

import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor // will work for final fields and create a SINGLE constructor that will get a DI
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    
    String authHeader = request.getHeader("Authorization");
    
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      // "Bearer " [0:6] => need to cut it off
      String jwt = authHeader.substring(7);
      
      try {
        // JWT validation performed on parsing by the library
        String username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          /*
           * isEnabled checks for additional requirements to be true (isEmailConfirmed, are there no bans etc (see User model))
           * We simply do not want to authenticate such token => skip this part and keep the user logged out.
           * 
           * During login process, authentication manager will throw an error, we send it as JSON to the client even tho it would not change much 
           * (issued JWT would still not get the user past the security filters)
           * 
           * Exactly the behaviour we need for our app (upon loggin,. we want to inform the user the reason auth was declined, 
           * for other responses, he will just see the pages as a "guest")
           */ 

          if (userDetails.isEnabled() && userDetails.isAccountNonLocked()) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
          }
          
        }
      } catch (Exception e) {
        /* 
         * 2 possible scenarios:
         * 1) JWT validation failed 
         * 2) User with extracted username does not exist
         * Either way, we do not need to do anything, simply move to the next filter without editing the authentication part in the Security Context
         * 
         * But out of testing purposes will keep the cout statement here:
         */
        System.out.println("User not authenticated due to -> " + e.getClass().getSimpleName() + ": " + e.getMessage());

      }
    }
    filterChain.doFilter(request, response);
  }
  
}
