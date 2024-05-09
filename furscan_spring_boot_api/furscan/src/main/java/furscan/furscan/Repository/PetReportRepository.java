package furscan.furscan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import furscan.furscan.Entity.PetReport;

@Repository
public interface PetReportRepository extends CrudRepository<PetReport, Integer>{
     
     /**
      * This function is for list of the pets for the particular user.
      * @param id
      * @return
      */
     @Query(value = "select * from pet_report where user_id = ?1", nativeQuery = true)
     List<PetReport> index(Integer id);      
     
     /**
      * Function send modifies the flag to send it to the doctor.
      * @param id
      */
     @Modifying
     @Query(value = "update pet_report set is_send = 1 where pet_report_id = ?1", nativeQuery = true)
     void sendToDoctor(Integer id);


}
