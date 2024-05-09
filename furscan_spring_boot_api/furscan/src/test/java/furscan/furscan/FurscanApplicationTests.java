package furscan.furscan;

import com.fasterxml.jackson.databind.ObjectMapper;
import furscan.furscan.DTO.LoginRequestDto;
import furscan.furscan.Entity.MstUsers;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static furscan.furscan.Constants.MOCK_USER_LIST_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FurscanApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void signUp() throws Exception {
        MstUsers signupRequestDto = new MstUsers();

        signupRequestDto.setFirst_name("samit");
        signupRequestDto.setLast_name("mhatre");
        signupRequestDto.setEmail("samitm@gmail.com");
        signupRequestDto.setPassword("12345");
        signupRequestDto.setPhone_no("123456");
        signupRequestDto.setIs_doctor(0);

        String signUpJson = objectMapper.writeValueAsString(signupRequestDto);

        mockMvc.perform(post("/create-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpJson))
                .andExpect(status().isOk())
                .andDo(print());

    }

    public String getToken() throws Exception {


        LoginRequestDto login = new LoginRequestDto();


        login.setEmail("samitm@gmail.com");
        login.setPassword("12345");

        String loginJson = objectMapper.writeValueAsString(login);

        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();

        Map<String, Object> loginResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

        LinkedHashMap body = (LinkedHashMap) loginResponse.get("data");

        return body.get("accessToken").toString();
    }

    @Test
    public void userListTest() throws Exception {

        String token = getToken();

        MvcResult result = mockMvc.perform(get("/user-list")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();

        Map<String, Object> res = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        ArrayList body = (ArrayList) res.get("data");

        assertEquals(MOCK_USER_LIST_SIZE, body.size());
    }

    @Test
    public void userDetailsTest() throws Exception {

        String token = getToken();

        MvcResult result = mockMvc.perform(get("/details/101")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();

        Optional<MstUsers> res = objectMapper.readValue(result.getResponse().getContentAsString(), Optional.class);

        assertNotNull(res.get());
    }

    @Test
    public void userUpdateTest() throws Exception {

        String token = getToken();
        MstUsers mstUsers = new MstUsers();
        mstUsers.setFirst_name("rahul");
        String mstUsersJson = objectMapper.writeValueAsString(mstUsers);
        MvcResult result = mockMvc.perform(post("/update/101")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mstUsersJson))
                .andDo(print()).andReturn();

        Map<String, Object> res = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        LinkedHashMap body = (LinkedHashMap) res.get("data");

        assertEquals("rahul", body.get("first_name"));
    }

/*    @Ignore
    @Test
    public void userResetPasswordTest() throws Exception {

        String token = getToken();
        MstUsers mstUsers = new MstUsers();
        mstUsers.setPassword("654321");

        MvcResult result = mockMvc.perform(post("/reset-password?email=samitm@gmail.com&password=654321")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        Map<String, Object> res = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        Integer data = (Integer) res.get("data");
        String status = (String) res.get("status");
        assertEquals(2, data);
        assertEquals("Success", status);
    }*/



}
