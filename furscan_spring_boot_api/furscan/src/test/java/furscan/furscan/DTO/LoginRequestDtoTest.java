package furscan.furscan.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginRequestDtoTest {

    @Mock
    private LoginRequestDto loginRequestDto;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @BeforeEach
    public void init() {
        firstName = "John";
        lastName = "Doe";
        email = "jd@gmail.com";
        password = "qwerty";
        loginRequestDto = new LoginRequestDto(email, password, firstName, lastName);
    }

    @Test
    public void test_getFirst_name(){
        assertEquals(firstName, loginRequestDto.getFirst_name());
    }

    @Test
    public void test_setFirst_name(){
        String updatedFirstName = "Jane";
        loginRequestDto.setFirst_name(updatedFirstName);
        assertEquals(updatedFirstName, loginRequestDto.getFirst_name());
    }

    @Test
    public void test_getLast_name(){
        assertEquals(lastName, loginRequestDto.getLast_name());
    }

    @Test
    public void test_setLast_name(){
        String updatedLastName = "Smith";
        loginRequestDto.setLast_name(updatedLastName);
        assertEquals(updatedLastName, loginRequestDto.getLast_name());
    }

    @Test
    public void test_getEmail(){
        assertEquals(email, loginRequestDto.getEmail());
    }

    @Test
    public void test_setEmail(){
        String updatedEmail = "js@gmail.com";
        loginRequestDto.setEmail(updatedEmail);
        assertEquals(updatedEmail, loginRequestDto.getEmail());
    }

    @Test
    public void test_getPassword(){
        assertEquals(password, loginRequestDto.getPassword());
    }

    @Test
    public void test_setPassword(){
        String updatedPassword = "1234";
        loginRequestDto.setPassword(updatedPassword);
        assertEquals(updatedPassword, loginRequestDto.getPassword());
    }

    @Test
    public void test_NoArgsConstructor() {
        LoginRequestDto instance = new LoginRequestDto();
        assertThat(instance).isNotNull();
    }
}
