package org.infrastructure.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.infrastructure.dto.ServerDTO;
import org.model.ServerSettings;
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

    private String jsonUsersPath;

    /**
     * Constructeur du repository (crée un dossier ue36 si pas existant)
     */
    public JsonRepository() throws IOException {
        Path path = Path.of(Paths.get("").toString(), "data", "server.json").toAbsolutePath();

        Files.createDirectories(path); //exception remontées
        if(!Files.exists(path)) {
            Files.createFile(path);
        }

        jsonUsersPath = path.toString();
    }

    /**
     * Constructeur de test repository
     * @param jsonBooksPath (Path) Chemin d'accès du fichier de sauvegarde
     */
    public JsonRepository(Path jsonBooksPath) {
        this.jsonUsersPath = jsonBooksPath.toString();
    }

    /**
     * Récupère les informations du serveur et ses utilisateurs
     * @return (ServerSettings) Objet paramètres serveur
     * @throws NotRetrievedException Impossible de récupérer les paramètres et utilisateurs du serveur
     */
    @Override
    public ServerSettings getServerSettings() throws NotRetrievedException {
        try(Reader reader = new BufferedReader(new FileReader(jsonUsersPath))) {
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
    public void saveServerSettings(ServerSettings serverSettings) throws NotSavedException {
        try(FileWriter fw = new FileWriter(jsonUsersPath, StandardCharsets.UTF_8)) {
            gson.toJson(DtoMapper.SeverSettingsToDto(serverSettings), fw);
        } catch (JsonIOException | IOException e) {
            throw new NotSavedException("Impossible de sauvegarder l'état du serveur", e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
