package furscan.furscan.Validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import furscan.furscan.Entity.MstUsers;
import furscan.furscan.Utils.Error;

@Component
public class UserValidation {

     /**
      * Validation request
      * @param mstUsers
      * @return
      */
     public List<Error> createUpdateRequest(MstUsers mstUsers) {
          List<Error> errors = new ArrayList<>();

          if(mstUsers.getFirst_name() == null){
               Error error = new Error("first_name", "First Name is Null");
               errors.add(error);
          }
          return errors;
     }
     
}
