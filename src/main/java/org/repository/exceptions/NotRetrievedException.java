package org.repository.exceptions;

/**
 * Exception Récupèration donnée invalide
 */
public class NotRetrievedException extends Exception{
    /**
     * Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     * @param e (Exception) déclencheur
     */
    public NotRetrievedException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}