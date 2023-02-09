package org.repository;

import org.infrastructure.dto.UserDTO;
import org.model.User;
import org.repository.exceptions.NotRetrievedException;
import org.repository.exceptions.NotSavedException;

import java.util.List;


/**
 * Interface repository
 */
public interface DataInterface extends AutoCloseable{

    /**
     * Sauvegarde un utilisateur
     * @param user (User) utilisateur à sauvegarder
     * @throws NotSavedException
     */
    void saveUser(User user) throws NotSavedException;

    /**
     * Récupère tous les utilisateurs stockés
     * @return (List<UserDTO>) Liste d'objet DTO User
     * @throws NotRetrievedException Impossible de récupérer les utilisateurs
     */
    List<UserDTO> getUsers() throws NotRetrievedException;
}
