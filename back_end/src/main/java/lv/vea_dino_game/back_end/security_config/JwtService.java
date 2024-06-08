package lv.vea_dino_game.back_end.security_config;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.*;

@Service
public class JwtService {

  private final String jwtSecret;
  
  public JwtService( @Value("a0cb8f1216516d6151b908b581e02d3d560ee0da80cc9d65872eb1c0fe7eb242") String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  private Key createKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public Claims extractAllClaims(String jwt) {
    return Jwts
            .parserBuilder()
            .setSigningKey(createKey()) // at this step Jwts should also validate the integrity of header and payload
            .build()
            .parseClaimsJws(jwt)
            .getBody();
  }
    
  public String extractUsername(String jwt) {
    // ..for now will consider username to be a JWT subject, might change it to an id later
    return extractAllClaims(jwt).getSubject();
  }

  public <T> T extractAnyClaim(String jwt, String claimName,  Class<T> type) {
    return extractAllClaims(jwt).get(claimName, type);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    final Integer hoursValidFor = 48;
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setIssuedAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * hoursValidFor))
            .signWith(createKey(), SignatureAlgorithm.HS256)
            .compact();
  }
    
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  private boolean isJwtExpired(String jwt) {
    return extractAllClaims(jwt).getExpiration().before(new Date());
  }

  public boolean isJwtValid(String jwt) {
    try {
      /*
       * If the token is invalid due to malformation / invalid signature or anything 
       * else, isJwtExpired will throw an error before returning a boolean. 
       */
      if(isJwtExpired(jwt)) {
        /*
         * AT this point, the issue is with the expiry date
         */
        throw new AuthenticationCredentialsNotFoundException("JWT is expired");
      }
      return true;
    } 
    catch(AuthenticationCredentialsNotFoundException e) {
      throw e;
    }
    catch (Exception e) {
      throw new AuthenticationCredentialsNotFoundException("JWT invalid");
    }
  }
}
