package org.model.exceptions;

/**
 * Exception Récupèration donnée param serveur invalide
 */
public class InvalidServerSettingsException extends Exception{

    /**
     * Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     */
    public InvalidServerSettingsException(String errorMessage) {
        super(errorMessage);
    }
}