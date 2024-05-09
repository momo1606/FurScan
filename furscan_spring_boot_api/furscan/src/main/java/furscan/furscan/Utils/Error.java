package furscan.furscan.Utils;

public class Error {
     private String target;
     private String message;

     public String getTarget() {
          return this.target;
     }

     public void setTarget(String target) {
          this.target = target;
     }

     public String getMessage() {
          return this.message;
     }

     public void setMessage(String message) {
          this.message = message;
     }

     /**
      * Error function
      * @param target
      * @param message
      */
     public Error(String target, String message) {
          this.target = target;
          this.message = message;
     }

}
