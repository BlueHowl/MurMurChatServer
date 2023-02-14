package org.model;

import org.client.ClientRunnable;
import org.model.exceptions.InvalidUserException;
import org.repository.exceptions.NotSavedException;
import org.utils.Queries;
import org.utils.RandomStringUtil;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface Task qui permet de récupérer le type de la tache et de l'exécuter
 */
public class Task {
    private final int id;
    private final Map<String, String> commandMap;
    private final ClientRunnable client;
    private String status;

    public Task(int id, Map<String, String> commandMap, ClientRunnable client, String status) {
        this.id = id;
        this.commandMap = commandMap;
        this.client = client;
        this.status = status;
    }

    /**
     * Récupère la map des valeurs de la commande associée
     * @return (Map<String, String>) commandMap
     */
    public Map<String, String> getCommandMap() {
        return commandMap;
    }

    /**
     * Récupère le thread client
     * @return (ClientRunnable)
     */
    public ClientRunnable getClient() {
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