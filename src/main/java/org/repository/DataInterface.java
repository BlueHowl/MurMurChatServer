package org.repository;

import org.model.Server;
import org.repository.exceptions.NotRetrievedException;
import org.repository.exceptions.NotSavedException;



/**
 * Interface repository
 */
public interface DataInterface extends AutoCloseable{

    /**
     * Récupère les informations du serveur et ses utilisateurs
     * @return (ServerSettings) Objet paramètres serveur
     * @throws NotRetrievedException Impossible de récupérer les paramètres et utilisateurs du serveur
     */
    Server getServerSettings() throws NotRetrievedException;

    /**
     * Sauvegarde les paramètres serveur et ses utilisateurs
     * @param serverSettings (ServerSettings) Objet paramètres serveur
     * @throws NotSavedException Impossible de sauvegarder les paramètres et utilisateurs du serveur
     */
    void saveServerSettings(Server serverSettings) throws NotSavedException;
}
