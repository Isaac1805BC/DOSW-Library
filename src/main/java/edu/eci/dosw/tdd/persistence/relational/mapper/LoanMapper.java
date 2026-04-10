package edu.eci.dosw.tdd.persistence.relational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.relational.entity.LoanEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class, UserMapper.class})
public interface LoanMapper {

    LoanEntity toEntity(Loan loan);

    Loan toModel(LoanEntity entity);

    List<Loan> toModelList(List<LoanEntity> entities);
}
