package org.task;

/**
 * Interface Task qui permet de récupérer le type de la tache et de l'exécuter
 */
public class Task {
    private int id;
    private String type;
    private String source;
    private String destination;
    private String status;
    public Task(int id, String type, String source, String destination, String status) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.destination = destination;
        this.status = status;
    }

    /**
     * Cette méthode permet de récupérer le type de la tache
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Cette méthode permet d'exécuter la tache
     */
    void execute() {

    }
}