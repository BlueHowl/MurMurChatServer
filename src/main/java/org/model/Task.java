package org.model;

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
     * @return le type de la tâche
     */
    public String getType() {
        return type;
    }

    /**
     * Cette méthode retourne les informations de transit de la tâches
     * @return un array source-> destination de la tâche
     */
    public String[] getTransitInfos() {
        return new String[] { source, destination };
    }

    /**
     * Cette méthode permet d'exécuter la tache
     */
    public void execute() {

    }

    public enum Types {
        
    }
}