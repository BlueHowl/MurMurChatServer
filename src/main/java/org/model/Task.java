package org.model;

import org.sharedClients.SharedRunnableInterface;

import java.util.Map;

/**
 * Interface Task qui permet de récupérer le type de la tache et de l'exécuter
 */
public class Task {
    private final int id;
    private final Map<String, Object> commandMap;
    private final SharedRunnableInterface client;
    private String status;

    public Task(int id, Map<String, Object> commandMap, SharedRunnableInterface client, String status) {
        this.id = id;
        this.commandMap = commandMap;
        this.client = client;
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
     * Récupère le thread client
     * @return (SharedRunnableInterface)
     */
    public SharedRunnableInterface getClient() {
        return client;
    }

    /**
     * Récupère le status de la tache
     * @return (String)
     */
    public String getStatus() {
        return status;
    }

}