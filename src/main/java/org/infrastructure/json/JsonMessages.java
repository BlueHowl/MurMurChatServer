package org.infrastructure.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMessages implements AutoCloseable{

    private final Gson gson = new Gson();
    private final String jsonPath;

    /**
     * Constructeur de la classe JsonMessages
     */
    public JsonMessages() {
        Path path = Path.of(Paths.get("").toString(), "data", "messages.json").toAbsolutePath();

        try {
            Files.createDirectories(path.getParent());

            if(!Files.exists(path)) {
                Files.createFile(path);
            }

        } catch (IOException ignored) {}

        jsonPath = path.toString();
    }

    /**
     * Récupère les messages stockés dans le fichier json
     * @return (Map<String, List<String>>)
     */
    public Map<String, List<String>> getMessages () {
        try(Reader reader = new BufferedReader(new FileReader(jsonPath))) {
            return gson.fromJson(reader, new TypeToken<Map<String, List<String>>>(){}.getType());
        } catch (JsonIOException | IOException e) {
            System.out.println("Erreur lors de la récupération des messages");
            return new HashMap<>();
        }
    }

    /**
     * Sauvegarde les messages dans le fichier json
     * @param messages (Map<String, List<String>>)
     */
    public void saveMessages (Map<String, List<String>> messages) {
        try(FileWriter fw = new FileWriter(jsonPath, StandardCharsets.UTF_8)) {
            gson.toJson(messages, fw);
        } catch (JsonIOException | IOException e) {
            System.out.println("Erreur lors de la sauvegarde des messages");
        }
    }

    @Override
    public void close() throws Exception {

    }
}
