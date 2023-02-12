package org.thread;

/**
 * Interface TaskFactory
 * Permet de créer des taches
 */
public interface TaskFactoryInterface {

    /**
     * Crée une nouvelle tache et la met dans la queue
     * @param typeSended (String)
     * @param source (String)
     * @param destination (String)
     */
    void createTask(String typeSended, String source, String destination);

}
