package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("relational")
public interface LoanRepository extends JpaRepository<LoanEntity, String> {
    List<LoanEntity> findByUser_Id(String userId);
}
