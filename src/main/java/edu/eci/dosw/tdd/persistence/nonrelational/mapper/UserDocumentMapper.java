package edu.eci.dosw.tdd.persistence.nonrelational.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.nonrelational.document.UserDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDocumentMapper {

    UserDocument toDocument(User user);

    User toDomain(UserDocument document);

    List<User> toDomainList(List<UserDocument> documents);
}
