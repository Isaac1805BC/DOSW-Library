package edu.eci.dosw.tdd.persistence.repository;

import edu.eci.dosw.tdd.core.model.Status;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByUser_Id(String userId);
    
    @Query("SELECT l FROM LoanEntity l WHERE l.user.id = :userId AND l.status = :status")
    List<LoanEntity> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") Status status);
}
