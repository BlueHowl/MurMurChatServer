package org.model.exceptions;

/**
 * Exception Récupèration donnée utilisateur invalide
 */
public class UserNotValidException extends Exception{

    /**
     * Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public UserNotValidException(String errorMessage) {
        super(errorMessage);
    }
}