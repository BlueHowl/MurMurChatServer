package org.infrastructure.json;

import org.infrastructure.dto.UserDTO;
import org.model.User;
import org.model.exceptions.UserNotValidException;

/**
 * Classe de mapping DTO
 */
public class DtoMapper {

    //Book
    /**
     * Converti l'objet user en objet DTO user
     * @param user (User) utilisateur
     * @return (UserDTO) utilisateur dto
     */
    public static UserDTO bookToDto(User user) {
        return new UserDTO(user.getUsername(), user.getBcryptRotations(), user.getBcryptSalt(), user.getBcryptHash());
    }

    /**
     * Converti l'objet DTO user en objet user
     * @param userDto (UserDTO) Utilisateur dto
     * @return (User) Utilisateur
     */
    public static User dtoToUser(UserDTO userDto) throws UserNotValidException {
        return new User(userDto.getUsername(), userDto.getBcryptRotations(), userDto.getBcryptSalt(), userDto.getBcryptHash());
    }

}
