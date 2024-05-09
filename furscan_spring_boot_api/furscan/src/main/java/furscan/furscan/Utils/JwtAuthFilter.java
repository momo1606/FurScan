package furscan.furscan.Utils;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Security.UserInfoUserDetailsService;

import static furscan.furscan.Constants.JWT_TOKEN_START_INDEX;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {


//      private JwtUtil jwtUtil;

//      @Autowired
//      public JwtAuthFilter(JwtUtil jwtUtil)
//      {
//           this.jwtUtil = jwtUtil;
//      }

//     @Override
//      protected void doFilterInternal(
//           HttpServletRequest request,
//           HttpServletResponse response,
//           FilterChain filterChain) throws ServletException, IOException {
//           String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//           if (header != null) {
//                String[] authElements = header.split(" ");
//                if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
//                     try {
//                          String token = authElements[1];
//                          System.out.println("token" + token);
//                               if(jwtUtil.validateToken(token)){
//                                    MstUsers users = jwtUtil.getUserByToken(authElements[1]);
//                                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users, null, Collections.emptyList());
//                                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                               }
//                     } catch (RuntimeException e) {
//                          SecurityContextHolder.clearContext();
//                          throw e;
//                     } catch (Exception e) {
//                          e.printStackTrace();
//                     }
//                }
//           }
//           filterChain.doFilter(request, response);
//      }

     @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserInfoUserDetailsService userDetailsService;

    /**
     * To do the Filter and setting up the Bearer Token.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        MstUsers username = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(JWT_TOKEN_START_INDEX);
            username = jwtUtil.getUserByToken(token);
        }
        try {
          
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username.getEmail());
            if (jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken;
                authToken = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        } catch (RuntimeException e) {
                         SecurityContextHolder.clearContext();
                         throw e;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Set the Jwt token for the authentication.
     * @param jwtUtil
    */
    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * To get the User Details
     * @param userDetailsService
     */
    public void setUserDetailsService(UserInfoUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}

