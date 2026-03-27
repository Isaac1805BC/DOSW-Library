package edu.eci.dosw.tdd.persistence.mapper;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(User user);

    @Mapping(target = "password", ignore = true)
    User toModel(UserEntity entity);

    @Mapping(target = "password", ignore = true)
    List<User> toModelList(List<UserEntity> entities);
}
