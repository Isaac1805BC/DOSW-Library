package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.Loan;
import edu.eci.dosw.tdd.persistence.nonrelational.document.LoanDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BookDocumentMapper.class, UserDocumentMapper.class})
public interface LoanDocumentMapper {

    LoanDocument toDocument(Loan loan);

    Loan toDomain(LoanDocument document);

    List<Loan> toDomainList(List<LoanDocument> documents);
}
