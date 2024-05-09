package furscan.furscan.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import furscan.furscan.Utils.JwtAuthFilter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Autowired
     private JwtAuthFilter jwtAuthFilter;
     @Autowired
     private UserAuthenticationEntryPoint userAuthenticationEntryPoint;
     
     // @Bean
     // public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     //      http
     //           .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
     //           .addFilterBefore(new JwtAuthFilter(jwtUtil), BasicAuthenticationFilter.class)
     //           .csrf(AbstractHttpConfigurer::disable)
     //           .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
     //           .authorizeHttpRequests((requests) -> requests
     //                .requestMatchers(HttpMethod.POST, "/login","/create-user").permitAll()
     //                .requestMatchers(HttpMethod.GET, "/user-list").permitAll()
     //                .anyRequest().authenticated())
     //                .httpBasic();
     //           http.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
     //      return http.build();
     // }

     // @Bean
     // public PasswordEncoder passwordEncoder() {
     //      return new BCryptPasswordEncoder();
     // }

     // @Bean
     // public JwtAuthFilter jwtAuthFilter() {
     //      return new JwtAuthFilter(jwtUtil);
     // }

     // @Bean
     // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
     //      return authenticationConfiguration.getAuthenticationManager();
     // }

     /**
      * Gets the User details.
      * @return
      */
     @Bean
    //authentication
    public UserDetailsService userDetailsService() {
//        UserDetails admin = User.withUsername("Basant")
//                .password(encoder.encode("Pwd1"))
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withUsername("John")
//                .password(encoder.encode("Pwd2"))
//                .roles("USER","ADMIN","HR")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
        return new UserInfoUserDetailsService();
    }

    /**
     * Verifies the request authorization.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     //    return http.csrf().disable()
     //            .authorizeHttpRequests()
     //            .requestMatchers(HttpMethod.POST,"/login").permitAll()
     //            .and()
     //            .authorizeHttpRequests().requestMatchers("/login")
     //            .authenticated().and()
     //            .sessionManagement()
     //            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
     //            .and()
     //            .authenticationProvider(authenticationProvider())
     //            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
     //            .build();
          http
               .cors().and()
               .exceptionHandling(customizer -> customizer.authenticationEntryPoint(userAuthenticationEntryPoint))
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests((requests) -> requests
                    .requestMatchers(HttpMethod.POST, "/login","/create-user", "reset-password","otp-verification", "otp").permitAll()
                    .requestMatchers(HttpMethod.GET, "/user-list").permitAll()
                    .anyRequest().authenticated())
                    .httpBasic();
          return http.build();
    }

    /**
     * Encrypt the password
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Does the data access object authentication for the user request
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Manages the Authenctication
     * @param config
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//     @Bean
//      public JwtAuthFilter jwtAuthFilter() {
//           return new JwtAuthFilter();
//      }
     
}
