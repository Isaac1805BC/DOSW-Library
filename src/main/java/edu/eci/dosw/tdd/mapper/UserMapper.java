package edu.eci.dosw.tdd.mapper;

import edu.eci.dosw.tdd.dto.UserDTO;
import edu.eci.dosw.tdd.core.model.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return new UserDTO();
    }
}
