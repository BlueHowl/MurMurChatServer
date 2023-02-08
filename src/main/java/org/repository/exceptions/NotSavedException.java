package org.repository.exceptions;

/**
 * Exception Sauvegarde donnée invalide
 */
public class NotSavedException extends Exception{
    /**
     *Remonte l'erreur donnée en paramètres
     * @param errorMessage (String) message d'erreur
     * @param e (Exception) déclencheur
     */
    public NotSavedException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }
}
