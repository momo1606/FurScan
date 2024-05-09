package furscan.furscan.Validator;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Utils.Error;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
//import static org.powermock.api.mockito.PowerMockito.whenNew;

class UserValidationTest {

    @InjectMocks
    private UserValidation userValidation;

    @Test
    public void createUpdateRequest_FirstNameErrorTest() throws Exception {
        MstUsers mstUsers = new MstUsers();
        List<Error> errors = new ArrayList<>();
        Error error = new Error("first_name", "First Name is Null");
        errors.add(error);
        //whenNew(Error.class).withArguments(any(String.class),any(String.class)).thenReturn(error);
        userValidation=new UserValidation();
        List<Error> error_actual = userValidation.createUpdateRequest(mstUsers);
        assertNotNull(error_actual);
    }

    @Test
    public void createUpdateRequest_NoErrorTest() throws Exception {
        MstUsers mstUsers = new MstUsers((Integer)1, "First", "last","test@gmail.com","password", "1234567890", "String profile_image_text", (Integer)1, (Integer) 1,  null, null,null,1);
        userValidation=new UserValidation();
        List<Error> error_actual = userValidation.createUpdateRequest(mstUsers);
        assertNotNull(error_actual);
    }
}