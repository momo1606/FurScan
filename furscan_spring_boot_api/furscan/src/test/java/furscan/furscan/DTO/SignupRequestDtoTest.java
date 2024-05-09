package furscan.furscan.DTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignupRequestDtoTest {

    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String phone_no;
    private String profile_image_text;
    private Integer is_doctor;
    private Integer is_active;

    @InjectMocks
    private SignupRequestDto signupRequestDto;

    @BeforeEach
    public void init(){
        first_name = "John";
        last_name = "Doe";
        email = "jd@gmail.com";
        password = "1234";
        phone_no = "9876543210";
        profile_image_text = "profileImageText";
        is_doctor = 0;
        is_active = 1;
        signupRequestDto = new SignupRequestDto(first_name, last_name, email, password, phone_no, profile_image_text, is_doctor, is_active);
    }

    @Test
    public void test_getProfile_image_text(){
        assertEquals(profile_image_text, signupRequestDto.getProfile_image_text());
    }

    @Test
    public void test_setProfile_image_text(){
        String updatedProfileImageText = "updated_profileImageText";
        signupRequestDto.setProfile_image_text(updatedProfileImageText);
        assertEquals(updatedProfileImageText, signupRequestDto.getProfile_image_text());
    }

    @Test
    public void test_getIs_doctor(){
        assertEquals(is_doctor, signupRequestDto.getIs_doctor());
    }

    @Test
    public void test_setIs_doctor(){
        Integer updatedDoctor = 1;
        signupRequestDto.setIs_doctor(updatedDoctor);
        assertEquals(updatedDoctor, signupRequestDto.getIs_doctor());
    }

    @Test
    public void test_getIs_active(){
        assertEquals(is_active, signupRequestDto.getIs_active());
    }

    @Test
    public void test_setIs_active(){
        Integer updatedStatus = 0;
        signupRequestDto.setIs_active(updatedStatus);
        assertEquals(updatedStatus, signupRequestDto.getIs_active());
    }

    @Test
    public void test_getFirst_name(){
        assertEquals(first_name, signupRequestDto.getFirst_name());
    }

    @Test
    public void test_setFirst_name(){
        String updateFirstName = "Jane";
        signupRequestDto.setFirst_name(updateFirstName);
        assertEquals(updateFirstName, signupRequestDto.getFirst_name());
    }

    @Test
    public void test_getLast_name(){
        assertEquals(last_name, signupRequestDto.getLast_name());
    }

    @Test
    public void test_setLast_name(){
        String updatedLastName = "Smith";
        signupRequestDto.setLast_name(updatedLastName);
        assertEquals(updatedLastName, signupRequestDto.getLast_name());

    }

    @Test
    public void test_getEmail(){
        assertEquals(email, signupRequestDto.getEmail());
    }

    @Test
    public void test_setEmail(){
        String updatedEmail = "jane@gmail.com";
        signupRequestDto.setEmail(updatedEmail);
        assertEquals(updatedEmail, signupRequestDto.getEmail());
    }

    @Test
    public void test_getPassword(){
        assertEquals(password, signupRequestDto.getPassword());
    }

    @Test
    public void test_setPassword(){
        String updatedPassword = "qwerty";
        signupRequestDto.setPassword(updatedPassword);
        assertEquals(updatedPassword, signupRequestDto.getPassword());
    }

    @Test
    public void test_getPhone_no(){
        assertEquals(phone_no, signupRequestDto.getPhone_no());
    }

    @Test
    public void test_setPhone_no(){
        String updatedNumber = "9876543000";
        signupRequestDto.setPhone_no(updatedNumber);
        assertEquals(updatedNumber, signupRequestDto.getPhone_no());
    }

}
