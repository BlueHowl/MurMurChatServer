package org.infrastructure.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.infrastructure.dto.UserDTO;
import org.model.User;
import org.repository.DataInterface;
import org.repository.exceptions.NotSavedException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Repository Json
 * Gère la sauvegarde et la récupération des données sous format json
 */
public class JsonRepository implements DataInterface, AutoCloseable {

    private final Gson gson = new Gson();

    private String jsonBooksPath;

    /**
     * Constructeur du repository (crée un dossier ue36 si pas existant)
     */
    public JsonRepository() throws IOException {
        Files.createDirectories(Path.of(System.getProperty("user.home"), "murmur", "serverConfig.json")); //exception remontée
    }

    /**
     * Constructeur de test repository
     * @param jsonBooksPath (Path) Chemin d'accès du fichier de sauvegarde
     */
    public JsonRepository(Path jsonBooksPath) {
        this.jsonBooksPath = jsonBooksPath.toString();
    }


    /**
     * Sauvegarde l'utilisateur donné en l'ajoutant aux autres
     * @param user (User) Objet User
     * @throws NotSavedException Impossible de sauvegarder les utilisateurs
     */
    @Override
    public void saveUser(User user) throws NotSavedException {
        List<UserDTO> users;
        users = getUsers();

        if(users != null) {
            users.add(DtoMapper.bookToDto(user));
        } else {
            users = new ArrayList<>();
            users.add(DtoMapper.bookToDto(user));
        }

        saveUsers(users);
    }


    /**
     * Récupère tous les utilisateurs stockés
     * @return (List<UserDTO>) Liste d'objet DTO User
     */
    private List<UserDTO> getUsers() {//throws IOException {
        List<UserDTO> users = new ArrayList<>();

        try(Reader reader = new BufferedReader(new FileReader(jsonBooksPath))) {
            Type userDtoList = new TypeToken<ArrayList<UserDTO>>(){}.getType();
            users = gson.fromJson(reader, userDtoList);
        } catch (IOException ignored) {}

        return users;
    }

    /**
     * Sauvegarde tous les utilisateurs donnés
     * @param users (List<UserDTO>) Liste de tous les utilisateurs en Objet DTO
     * @throws IOException Impossible de sauvegarder les utilisateurs
     */
    private void saveUsers(List<UserDTO> users) throws NotSavedException {
        try(FileWriter fw = new FileWriter(jsonBooksPath, StandardCharsets.UTF_8)) {
            gson.toJson(users, fw);
        } catch (JsonIOException | IOException e) {
            throw new NotSavedException("Impossible de sauvegarder les utilisateurs", e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
