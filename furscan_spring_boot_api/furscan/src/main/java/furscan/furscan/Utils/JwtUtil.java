package furscan.furscan.Utils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Repository.AuthRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import static furscan.furscan.Constants.JWT_TOKEN_START_INDEX;

@RequiredArgsConstructor
@Component
public class JwtUtil {

     private final AuthRepository authRepository;
     
     Date now = new Date(System.currentTimeMillis());
     Date validity = new Date(now.getTime() + 1000*60*60*24*2);
     // System.out.println(now);
     private static String secret = "furscan";

     /**
      * To call the create token
      * @param user
      * @return
      */
     public String generateToken(MstUsers user) {
          Map<String, Object> claims = new HashMap<>();
          return createToken(claims, user);
     }

     /**
      * To create the token for the user who requested it.
      * @param claims
      * @param user
      * @return
      */
     public String createToken(Map<String, Object> claims, MstUsers user) {
          System.out.println(now);
          System.out.println(validity);
          claims.put("first_name", user.getFirst_name());
          claims.put("last_name", user.getLast_name());
          return Jwts.builder()
                  .setClaims(claims)
                  .setSubject(user.getEmail())
                  .setIssuedAt(now)
                  .setExpiration(validity)
                  .signWith(SignatureAlgorithm.HS256, secret)
                  .compact();
     }
     
     /**
      * Validating the token.
      * @param token
      * @return
      */
     public boolean validateToken(String token){
          try {
               Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
               return true;
          } catch (Exception e) {
               throw new AuthenticationCredentialsNotFoundException("Token has expired or incorrect");
          }
     }

     /**
      * Validating the token strongly with the inbuilt function.
      * @param token
      * @return
      */
     public Authentication validateTokenStrongly(String token) {
          MstUsers user = getUserByToken(token);
          return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
     }

     /**
      * Getting the user from the token generated.
      * @param token
      * @return
      */
     public MstUsers getUserByToken(String token) {
          Claims claim = Jwts.parser()
                              .setSigningKey(secret)
                              .parseClaimsJws(token)
                              .getBody();

          MstUsers user = authRepository.findOneByEmail(claim.getSubject());

          if(user == null) {
               return null;
          }

          return user;
     }

     /**
      * Get user.
      * @param request
      * @return
      */
     public MstUsers getUser(HttpServletRequest request) {
          String authHeader = request.getHeader("Authorization");
          String token = null;
          token = authHeader.substring(JWT_TOKEN_START_INDEX);
          MstUsers user = getUserByToken(token);
          return user;
     }
}
