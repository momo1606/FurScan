package furscan.furscan.Security;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Repository.AuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;

import static furscan.furscan.Constants.MSTUSERS_OTP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserInfoUserDetailsServiceTest {

    @Mock
    private MstUsers mstUsers;

    @Mock
    private AuthRepository authRepository;

//    @Mock
    private UserInfoUserDetails userInfoUserDetails;

    @InjectMocks
    private UserInfoUserDetailsService userInfoUserDetailsService;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        mstUsers = new MstUsers(1, "John", "Doe", "johndoe@gmail.com", "qwerty", "9876543210", "testImage", 0, 1, LocalDateTime.now(), null, LocalDateTime.now(), MSTUSERS_OTP);
        userInfoUserDetails = new UserInfoUserDetails(mstUsers);
    }

    @Test
    public void test_loadUserByUsername_ReturnSuccess(){
        String email = "johndoe@gmail.com";
        when(authRepository.findOneByEmail(email)).thenReturn(mstUsers);
        UserDetails user = userInfoUserDetailsService.loadUserByUsername(email);
        assertNotNull(userInfoUserDetailsService.loadUserByUsername(email));
    }

    @Test
    public void test_loadUserByUsername_ReturnUserNotFoundException(){
        String email = "johndoe@gmail.com";
        when(authRepository.findOneByEmail(email)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userInfoUserDetailsService.loadUserByUsername(email));
    }


}
