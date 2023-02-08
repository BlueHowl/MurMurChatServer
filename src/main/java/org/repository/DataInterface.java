package org.repository;

import org.model.User;
import org.repository.exceptions.NotSavedException;


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
}
