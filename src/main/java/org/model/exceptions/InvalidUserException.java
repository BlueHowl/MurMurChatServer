package org.model.exceptions;

/**
 * Exception Récupèration donnée utilisateur invalide
 */
public class InvalidUserException extends Exception{

    /**
     * Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public InvalidUserException(String errorMessage) {
        super(errorMessage);
    }
}