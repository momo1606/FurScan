package furscan.furscan.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import furscan.furscan.Entity.PetReport;

@Repository
public interface VetRepository extends CrudRepository<PetReport, Integer> {

    /**
     * To get the list of the pet for vet who have done send to doctor.
     * @return
     */
    @Query(value = "select * from pet_report where is_send = 1", nativeQuery = true)
    List<PetReport> is_SendReport();

    /**
     * Finding the details for individual pet.
     * @param id
     * @return
     */
    @Query(value = "select * from pet_report where pet_report_id = ?1", nativeQuery = true)
    PetReport findByPet_report_id(Integer id);
}