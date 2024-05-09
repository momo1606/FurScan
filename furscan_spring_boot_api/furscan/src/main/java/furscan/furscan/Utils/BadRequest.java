package furscan.furscan.Utils;

import java.util.List;

public class BadRequest extends RuntimeException{
     private List<Error> error;

     public List<Error> getError() {
          return this.error;
     }

     public void setError(List<Error> error) {
          this.error = error;
     }

     public BadRequest(String message, List<Error> errors){
          super(message);
          this.error = errors;
     }

}
