package org.server.exception;

/**
 * Exception Récupèration donnée utilisateur invalide
 */
public class CloseClientException extends Exception{

    /**
     * Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     * @param e (Exception)
     */
    public CloseClientException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}