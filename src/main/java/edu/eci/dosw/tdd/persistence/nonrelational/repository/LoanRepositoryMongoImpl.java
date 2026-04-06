package edu.eci.dosw.tdd.persistence.nonrelational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.port.ILoanRepository;
import edu.eci.dosw.tdd.persistence.nonrelational.mapper.LoanDocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("mongo")
@RequiredArgsConstructor
public class LoanRepositoryMongoImpl implements ILoanRepository {

    private final MongoLoanRepository mongoLoanRepository;
    private final LoanDocumentMapper loanDocumentMapper;

    @Override
    public Loan save(Loan loan) {
        return loanDocumentMapper.toDomain(
                mongoLoanRepository.save(loanDocumentMapper.toDocument(loan)));
    }

    @Override
    public Optional<Loan> findById(String id) {
        return mongoLoanRepository.findById(id).map(loanDocumentMapper::toDomain);
    }

    @Override
    public List<Loan> findAll() {
        return mongoLoanRepository.findAll()
                .stream()
                .map(loanDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return mongoLoanRepository.findByUserId(userId)
                .stream()
                .map(loanDocumentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        mongoLoanRepository.deleteById(id);
    }
}
