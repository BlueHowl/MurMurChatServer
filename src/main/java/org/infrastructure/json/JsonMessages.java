package org.infrastructure.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import org.model.exceptions.InvalidServerSettingsException;
import org.repository.exceptions.NotRetrievedException;
import org.repository.exceptions.NotSavedException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonMessages {

    private final Gson gson = new Gson();
    private final String jsonPath;

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

    public Map<String, List<String>> getMessages () {
        try(Reader reader = new BufferedReader(new FileReader(jsonPath))) {
            return gson.fromJson(reader, new TypeToken<Map<String, List<String>>>(){}.getType());
        } catch (JsonIOException | IOException e) {
            System.out.println("Erreur lors de la récupération des messages");
            return new HashMap<>();
        }
    }

    public void saveMessages (Map<String, List<String>> messages) {
        try(FileWriter fw = new FileWriter(jsonPath, StandardCharsets.UTF_8)) {
            gson.toJson(messages, fw);
        } catch (JsonIOException | IOException e) {
            System.out.println("Erreur lors de la sauvegarde des messages");
        }
    }
}
