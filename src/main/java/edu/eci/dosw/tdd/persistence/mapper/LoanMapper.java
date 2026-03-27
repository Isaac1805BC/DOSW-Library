package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.entity.LoanEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookMapper.class, UserMapper.class})
public interface LoanMapper {

    @Mapping(target = "id", ignore = true)
    LoanEntity toEntity(Loan loan);

    Loan toModel(LoanEntity entity);

    List<Loan> toModelList(List<LoanEntity> entities);
}
