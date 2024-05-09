package furscan.furscan.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import furscan.furscan.Entity.MstUsers;

@Repository
public interface UserRepository extends CrudRepository<MstUsers, Integer>{
     
     /**
      * Function gets the details for the user.
      * @param id
      * @return
      */
     @Query(value = "select * from mst_users where user_id = ?1", nativeQuery = true)
     List<MstUsers> me(Integer id);

     /**
      * Function gets the details of the pet list for the individual user.
      * @param user_id
      * @return
      */
     @Query(value = "select * from pet_report where user_id = ?1", nativeQuery = true)
     List<Map<String, Object>> petList(Integer user_id);

     /**
      * Finding the details of the user from the email.
      * @param email
      * @return
      */
     MstUsers findOneByEmail(String email);

     /**
      * Find by ID.
      */
     Optional<MstUsers> findById(Integer id);
}
