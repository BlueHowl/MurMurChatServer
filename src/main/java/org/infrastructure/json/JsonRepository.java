package org.infrastructure.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.infrastructure.dto.ServerDTO;
import org.model.Server;
import org.model.exceptions.InvalidServerSettingsException;
import org.model.exceptions.InvalidTagException;
import org.model.exceptions.InvalidUserException;
import org.repository.DataInterface;
import org.repository.exceptions.NotRetrievedException;
import org.repository.exceptions.NotSavedException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Classe Repository Json
 * Gère la sauvegarde et la récupération des données sous format json
 */
public class JsonRepository implements DataInterface, AutoCloseable {

    private final Gson gson = new Gson();

    private final String jsonPath;

    /**
     * Constructeur du repository (crée un dossier ue36 si pas existant)
     */
    public JsonRepository() {
        Path path = Path.of(Paths.get("").toString(), "data", "server.json").toAbsolutePath();

        try {
            Files.createDirectories(path.getParent());

            if(!Files.exists(path)) {
                Files.createFile(path);
            }

        } catch (IOException ignored) {}

        jsonPath = path.toString();
    }

    /**
     * Constructeur de test repository
     * @param jsonBooksPath (Path) Chemin d'accès du fichier de sauvegarde
     */
    public JsonRepository(Path jsonBooksPath) {
        this.jsonPath = jsonBooksPath.toString();
    }

    /**
     * Récupère les informations du serveur et ses utilisateurs
     * @return (ServerSettings) Objet paramètres serveur
     * @throws NotRetrievedException Impossible de récupérer les paramètres et utilisateurs du serveur
     */
    @Override
    public Server getServerSettings() throws NotRetrievedException {
        try(Reader reader = new BufferedReader(new FileReader(jsonPath))) {
            Type serverDtoType = new TypeToken<ServerDTO>(){}.getType();
            return DtoMapper.dtoToServerSettings(gson.fromJson(reader, serverDtoType));
        } catch (IOException e) {
            throw new NotRetrievedException("Impossible de récupérer les utilisateurs existants", e);
        } catch (InvalidUserException | InvalidServerSettingsException | InvalidTagException e) {
            throw new NotRetrievedException(e.getMessage(), e);
        }
    }

    /**
     * Sauvegarde les paramètres serveur et ses utilisateurs
     * @param serverSettings (ServerSettings) Objet paramètres serveur
     * @throws NotSavedException Impossible de sauvegarder les paramètres et utilisateurs du serveur
     */
    @Override
    public void saveServerSettings(Server serverSettings) throws NotSavedException {
        try(FileWriter fw = new FileWriter(jsonPath, StandardCharsets.UTF_8)) {
            gson.toJson(DtoMapper.SeverSettingsToDto(serverSettings), fw);
        } catch (JsonIOException | IOException e) {
            throw new NotSavedException("Impossible de sauvegarder l'état du serveur", e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
