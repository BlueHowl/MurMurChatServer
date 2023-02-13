package org.model;

import java.io.PrintWriter;

/**
 * Interface Task qui permet de récupérer le type de la tache et de l'exécuter
 */
public class Task {
    private int id;
    private String type;
    private String source;
    private PrintWriter destination;
    private String status;
    public Task(int id, String type, String source, PrintWriter destination, String status) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.destination = destination;
        this.status = status;
    }

    /**
     * Cette méthode permet de récupérer le type de la tache
     * @return le type de la tâche
     */
    public String getType() {
        return type;
    }

    public PrintWriter getDestination() {
        return destination;
    }
}