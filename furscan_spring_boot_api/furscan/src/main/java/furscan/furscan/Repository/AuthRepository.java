package furscan.furscan.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import furscan.furscan.Entity.MstUsers;

@Repository
public interface AuthRepository extends CrudRepository<MstUsers, Integer>{

     /**
      * This function is for finding the user details with the email and password.
      * @param email
      * @param password
      * @return
      */
     MstUsers findOneByEmailAndPassword(String email, String password);

     /**
      * This function is for finding the user details with the email only.
      * @param email
      * @return
      */
     MstUsers findOneByEmail(String email);
     
}
