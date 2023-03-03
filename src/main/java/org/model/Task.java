package org.model;

import java.util.Map;

/**
 * Interface Task qui permet de récupérer le type de la tache et de l'exécuter
 */
public class Task {
    private final int id;
    private final Map<String, Object> commandMap;
    private final String username;
    private String status;

    public Task(int id, Map<String, Object> commandMap, String username, String status) {
        this.id = id;
        this.commandMap = commandMap;
        this.username = username;
        this.status = status;
    }

    /**
     * Récupère la map des valeurs de la commande associée
     * @return (Map<String, String>) commandMap
     */
    public Map<String, Object> getCommandMap() {
        return commandMap;
    }

    /**
     * Récupère le client
     * @return (User)
     */
    public String getUsername() {
        return username;
    }

    /**
     * Récupère le status de la tache
     * @return (String)
     */
    public String getStatus() {
        return status;
    }

}