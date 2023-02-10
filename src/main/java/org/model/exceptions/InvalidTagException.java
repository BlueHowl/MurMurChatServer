package org.model.exceptions;

/**
 * Exception Récupèration donnée tag invalide
 */
public class InvalidTagException extends Exception{

    /**
     * Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public InvalidTagException(String errorMessage) {
        super(errorMessage);
    }
}