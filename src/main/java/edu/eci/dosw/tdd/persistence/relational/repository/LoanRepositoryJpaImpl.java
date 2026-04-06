package edu.eci.dosw.tdd.persistence.relational.repository;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.core.port.ILoanRepository;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import edu.eci.dosw.tdd.persistence.relational.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Profile("relational")
@RequiredArgsConstructor
public class LoanRepositoryJpaImpl implements ILoanRepository {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanMapper loanMapper;

    @Override
    public Loan save(Loan loan) {
        LoanEntity entity = new LoanEntity();
        entity.setId(loan.getId());
        entity.setLoanDate(loan.getLoanDate());
        entity.setReturnDate(loan.getReturnDate());
        entity.setStatus(loan.getStatus());

        if (loan.getBook() != null) {
            bookRepository.findById(loan.getBook().getId()).ifPresent(entity::setBook);
        }
        if (loan.getUser() != null) {
            userRepository.findById(loan.getUser().getId()).ifPresent(entity::setUser);
        }

        return loanMapper.toModel(loanRepository.save(entity));
    }

    @Override
    public Optional<Loan> findById(String id) {
        return loanRepository.findById(id).map(loanMapper::toModel);
    }

    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll()
                .stream()
                .map(loanMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findByUserId(String userId) {
        return loanRepository.findByUser_Id(userId)
                .stream()
                .map(loanMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        loanRepository.deleteById(id);
    }
}
