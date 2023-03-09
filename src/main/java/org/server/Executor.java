package org.server;

import org.client.ClientRunnable;
import org.model.ServerSettings;
import org.model.Tag;
import org.model.Task;
import org.model.User;
import org.model.exceptions.InvalidUserException;
import org.relay.RelayManager;
import org.repository.DataInterface;
import org.repository.exceptions.NotSavedException;
import org.server.exception.CloseClientException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Executor implements Runnable {

    private static final String CARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*,-./:;<=>?@[]^_`";
    private static final int RANDOM22SIZE = 22;
    private static final String HELLO = "HELLO %s %s\r\n";
    private static final String PARAM = "PARAM %d %s\r\n";
    private static final String MSGS = "MSGS %s %s\r\n";
    private static final String OK = "+OK[ %s]\r\n";
    private static final String ERR = "-ERR[ %s]\r\n";

    private static final String FOLLOW = "FOLLOW %s\r\n";

    private static final String SEND = "SEND %d@%s %s %s %s\r\n";

    private final TaskList taskList;

    private final ClientManager clientManager;

    private final RelayManager relayManager;

    private final ServerSettings serverSettings;

    private final DataInterface dataInterface;
    private String key; //todo prblm synchronisation, si 2 personnes veulent se connecter, la clé risque d'être changée au mauvais moment la clé devrait se trouver dans un espace individualisé (clientRunnable ou User)

    public Executor(TaskList taskList, ClientManager clientManager, RelayManager relayManager, ServerSettings server, DataInterface dataInterface) {
        this.taskList = taskList;
        this.clientManager = clientManager;
        this.relayManager = relayManager;
        this.serverSettings = server;
        this.dataInterface = dataInterface;
    }

    @Override
    public void run() {
       while(true) {
            Task task = taskList.getTask();
            if (task != null) {
                execute(task);
            }
           relayManager.sendToRelay(String.format(SEND, 1, serverSettings.getCurrentDomain(), "dummytest@"+serverSettings.getCurrentDomain(), "dummytest@"+serverSettings.getCurrentDomain(), "testmessage test@test"));

       }
    }

    /**
     * Execute la tache
     * @param task (Task)
     */
    private void execute(Task task) {
        Map<String, Object> commandMap = task.getCommandMap();
        ClientRunnable client = task.getClient();

        switch((String)commandMap.get("type")) {
            case "HELLO":
                hello(client);
                break;
            case "REGISTER":
                register(commandMap, client);
                break;
            case "CONNECT":
                connect(commandMap, client);
                break;
            case "CONFIRM":
                confirm(commandMap, client);
                break;
            case "MSG":
                msg(commandMap, client);
                break;
            case "FOLLOW":
                follow(commandMap, client);
                break;
            case "DISCONNECT":
                disconnect(client);
                break;

            case "SEND":
                send(commandMap);
                break;
        }
    }

    private void hello(ClientRunnable client) {
        key = generateRandomString(RANDOM22SIZE);
        client.send(String.format(HELLO, serverSettings.getCurrentDomain(), key));
    }

    private void register(Map<String, Object> commandMap, ClientRunnable client) {
        try {
            User user = new User((String)commandMap.get("username"), Integer.parseInt((String)commandMap.get("bcryptround")),
                    (String)commandMap.get("bcryptsalt"), (String)commandMap.get("bcrypthash"), new HashSet<>(),
                    new HashSet<>(), 0, null);
            serverSettings.addUser(user);
            dataInterface.saveServerSettings(serverSettings); //sauvegarde json
            client.send(String.format(OK, "Le compte est enregistré"));
            System.out.println("Compte enregistré");
            client.setUser(user);
        } catch (InvalidUserException | NotSavedException ex) {
            client.send(String.format(ERR, ex.getMessage()));
            System.out.println("Erreur envoyé, erreur d'enregistrement");
        }
    }

    private void connect(Map<String, Object> commandMap, ClientRunnable client) {
        try {
            User user = serverSettings.findUser((String)commandMap.get("username"));
            client.send(String.format(PARAM, user.getBcryptRotations(), user.getBcryptSalt()));
            client.setUser(user);
        } catch (InvalidUserException ex) {
            client.send(String.format(ERR, ex.getMessage()));
        }
        System.out.println("Sending PARAM");
    }

    private void confirm(Map<String, Object> commandMap, ClientRunnable client) {
        try {
            User user = client.getUser();
            String sha3hex = (String)commandMap.get("sha3hex");

            MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            byte[] hash = digest.digest((key + "$2b$" + user.getBcryptRotations() + "$" + user.getBcryptSalt() + user.getBcryptHash()).getBytes(StandardCharsets.UTF_8));
            String comparable = bytesToHex(hash);
            if (sha3hex.equals(comparable)) {
                client.send(String.format(OK, "Welcome!"));
                System.out.println("Sending +OK");
            } else{
                client.send(String.format(ERR, "Wrong password"));
                System.out.println("Sending -ERR: Wrong password!");
            }
        } catch (NoSuchAlgorithmException ex) {
            client.send(String.format(ERR, ex.getMessage()));
        }
    }

    /**
     * Envoyer un message
     * @param commandMap (Map<String, Object>) contient le message et les hashtags
     * @param client (ClientRunnable) client qui envoie le message
     */
    private void msg(Map<String, Object> commandMap, ClientRunnable client) {
        String nameDomain = String.format("%s@%s", client.getUsername(), serverSettings.getCurrentDomain());

        String msgs = String.format(MSGS, nameDomain, commandMap.get("message"));

        Set<String> destinations = new HashSet<>(); //préviens les envois dupliqués
        destinations.addAll(client.getFollowers());
        //TODO envoyer a chaque follower du client un message SEND avec comme destination le follower
        for (String follower : client.getFollowers()) {

        }
        destinations.addAll(serverSettings.getTagFollowers((String[])commandMap.get("hashtags")));
        List<String> noDuplicatesDestinations = new ArrayList<>(destinations);

        //TODO regarder pour remplacer par un Set
        //List<ClientRunnable> clients = clientManager.getMatchingClients(serverSettings.getCurrentDomain(), new ArrayList<>(destinations));
        List<ClientRunnable> clients = clientManager.getMatchingClients(serverSettings.getCurrentDomain(), noDuplicatesDestinations);
        for (ClientRunnable c : clients) {
            c.send(msgs);
        }

        //send avec les noDuplicatesDestinations restantes
        //todo prblm si envoi jamais avec tag_domain dans la commande send ? faire différement ?
        // pr l'instant 1 requete par utilisateur mm si ils sont sur le mm serveur (snn envoyer une seul requete sur l'autre avec un tag)
        for(String destination : noDuplicatesDestinations) {
            relayManager.sendToRelay(String.format(SEND, 1, serverSettings.getCurrentDomain(), client.getUsername()+"@"+serverSettings.getCurrentDomain(), destination, msgs)); // todo gèrer id
        }
    }

    private void follow(Map<String, Object> commandMap, ClientRunnable client) {
        try {
            String domain = (String)commandMap.get("domain");

            if (commandMap.get("name") != null) {
                try {
                    User followedUser = serverSettings.findUser((String)commandMap.get("name"));
                    serverSettings.addFollowerToUser(followedUser, String.format("%s@%s", client.getUsername(), domain));
                }catch (InvalidUserException e) {
                    System.out.println(e.getMessage());
                    System.out.println("L'user est invalide");
                }
            } else {
                String followedTagString = (String)commandMap.get("tag");
                Tag tag = serverSettings.findTag(followedTagString);
                if (tag.getFollowers().isEmpty()) {
                    tag.addFollower(client.getUsername()+"@"+domain);
                    serverSettings.addTag(tag);
                    serverSettings.addUserTagToUser(client.getUser(), tag.getName()+"@"+domain);
                } else {
                    serverSettings.addFollowerToTag(tag, client.getUsername()+"@"+domain);
                    serverSettings.addUserTagToUser(client.getUser(), followedTagString+"@"+domain);
                }
            }
            dataInterface.saveServerSettings(serverSettings);
            System.out.println("Follow reçu");
        } catch (NotSavedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Déconnecte le client
     * @param client (ClientRunnable)
     */
    private void disconnect(ClientRunnable client) {

        try { //todo stop le thread ?
            client.send(String.format(OK, "GoodBye!"));
            clientManager.removeClient(client);
            System.out.printf("Déconnexion client : %s\n", client.getUsername());
        } catch (CloseClientException e) {
            System.out.printf("%s : %s\n", e.getMessage(), client.getUsername());
        }

    }

    private void send(Map<String, Object> commandMap) {
        if(commandMap.get("name") == null) {
            //gestion MSGS

            //récupère le client cible dans une liste, vide si non existant
            List<ClientRunnable> client = clientManager.getMatchingClients(serverSettings.getCurrentDomain(), List.of((String)commandMap.get("destnamedomain")));

            client.get(0).send((String)commandMap.get("internalmsg"));
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte h : hash) {
            String hex = Integer.toHexString(0xff & h);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Génère une chaine de caractères aléatoire de la taille donnée et contenant les caractères autorisés
     * @param length (int) Taille de la chaine à retourner
     * @return (String) chaine de caractères aléatoire
     */
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        random.setSeed(new Date().getTime());

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(CARACTERS.length());

            sb.append(CARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
