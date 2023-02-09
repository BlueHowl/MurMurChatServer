package org.task;

/**
 * Interface Task qui permet de récupérer le type de la tache et de l'exécuter
 */
public interface Task {

    /**
     * Cette méthode permet de récupérer le type de la tache
     * @return
     */
    String getType();

    /**
     * Cette méthode permet d'exécuter la tache
     */
    void execute();
}