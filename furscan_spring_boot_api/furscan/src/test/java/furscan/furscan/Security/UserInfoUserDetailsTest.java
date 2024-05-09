package furscan.furscan.Security;

import furscan.furscan.Entity.MstUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

import static furscan.furscan.Constants.MSTUSERS_OTP;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserInfoUserDetailsTest {

    @Mock
    private MstUsers mstUsers;

    @Mock
    private List<GrantedAuthority> authorities;

    @InjectMocks
    private UserInfoUserDetails userInfoUserDetails;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        mstUsers = new MstUsers(1, "John", "Doe", "johndoe@gmail.com", "qwerty", "9876543210", "testImage", 0, 1, LocalDateTime.now(), null, LocalDateTime.now(), MSTUSERS_OTP);
        userInfoUserDetails = new UserInfoUserDetails(mstUsers);
    }

    @Test
    public void test_getPassword(){
        assertEquals("qwerty", userInfoUserDetails.getPassword());
    }

    @Test
    public void test_getUsername(){
        assertEquals("johndoe@gmail.com", userInfoUserDetails.getUsername());
    }

    @Test
    public void test_isAccountNonExpired(){
        assertEquals(true, userInfoUserDetails.isAccountNonExpired());
    }

    @Test
    public void test_isAccountNonLocked(){
        assertEquals(true, userInfoUserDetails.isAccountNonLocked());
    }

    @Test
    public void test_isCredentialsNonExpired(){
        assertEquals(true, userInfoUserDetails.isCredentialsNonExpired());
    }

    @Test
    public void test_isEnabled(){
        assertEquals(true, userInfoUserDetails.isEnabled());
    }

}
