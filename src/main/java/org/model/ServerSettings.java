package org.model;

import org.infrastructure.json.JsonMessages;
import org.model.exceptions.InvalidUserException;

import java.util.*;

/**
 * Classe de stockage des paramètres serveurs et des utilisateurs et tags
 */
public class ServerSettings {
    private final String currentDomain;
    private final int saltSizeInBytes;
    private final String multicastAddress;
    private final int multicastPort;
    private final int unicastPort;
    private final int relayPort;
    private final String networkInterface;
    private final byte[] AESKey;
    private final boolean tls;
    private final Set<User> users;
    private final Set<Tag> tags;

    JsonMessages jsonMessages = new JsonMessages();

    public ServerSettings(String currentDomain, int saltSizeInBytes, String multicastAddress, int multicastPort, int unicastPort, int relayPort, String networkInterface, byte[] AESKey, boolean tls, Set<User> users, Set<Tag> tags) {
        this.currentDomain = currentDomain;
        this.saltSizeInBytes = saltSizeInBytes;
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        this.unicastPort = unicastPort;
        this.relayPort = relayPort;
        this.networkInterface = networkInterface;
        this.AESKey = AESKey;
        this.tls = tls;
        this.users = users;
        this.tags = tags;
    }


    //GETTERS

    /**
     * Récupère le nom de domaine du serveur
     * @return (String)
     */
    public String getCurrentDomain() {
        return currentDomain;
    }

    /**
     * Récupère la taille de Salt
     * @return int
     */
    public int getSaltSizeInBytes() {
        return saltSizeInBytes;
    }

    /**
     * Récupère l'adresse de multicast
     * @return String
     */
    public String getMulticastAddress() {
        return multicastAddress;
    }

    /**
     * Récupère le port multicast
     * @return (int)
     */
    public int getMulticastPort() {
        return multicastPort;
    }

    /**
     * Récupère le port unicast
     * @return (int)
     */
    public int getUnicastPort() {
        return unicastPort;
    }

    /**
     * Récupère le port du relai
     * @return (int)
     */
    public int getRelayPort() {
        return relayPort;
    }

    /**
     * Récupère l'interface réseaux à utiliser
     * @return (String)
     */
    public String getNetworkInterface() {
        return networkInterface;
    }

    /**
     * Récupère la clé AES en base 64
     * @return (String)
     */
    public byte[] getAESKey() {
        return AESKey;
    }

    /**
     * Récupère la valeur Tls
     * @return (boolean)
     */
    public boolean isTls() {
        return tls;
    }

    //USERS
    /**
     * Récupère la liste des utilisateurs DTO
     * @return (UserDTO)
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Vérifie si l'utilisateur est déjà un compte connu
     * @param user le compte
     * @return vrai s'il l'utilisateur est existant
     */
    private boolean userExist(User user) {
        return users.stream().anyMatch(n -> n.equals(user));
    }

    public User findUser(String username) throws InvalidUserException {
        Optional<User> u = users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
        if (u.isPresent())
            return u.get();
        else throw new InvalidUserException("Le compte n'existe pas");
    }

    public User findUserOnCompleteName(String follower) throws InvalidUserException {
        String username = follower.split("@")[0];
        return findUser(username);
    }

    //TAGS
    /**
     * Récupère la liste des tags DTO
     * @return (TagDTO)
     */
    public Set<Tag> getTags() {
        return tags;
    }

    public boolean tagExists(String tagString) {
       for (Tag tag : tags) {
            if(tag.getName().equals(tagString)) {
                return true;
            }
       }
       return false;
    }

    public Tag findTag(String tag) {
        if (tagExists(tag))
            return tags.stream().filter(t -> t.getName().equals(tag)).findFirst().get();
        else {
            System.out.println("Le tag n'existe pas");
            System.out.println("Création du nouveau tag");
            return new Tag(tag);
        }
    }

    /**
     * Récupère les followers du tag existants
     * @param hashtag (String) tag
     * @return (List<String>) liste de followers
     */
    public List<String> getTagFollowers(String hashtag) {
        /*if (!tagExists(hashtag))
            return null;
        */
        return new ArrayList<>(findTag(hashtag).getFollowers());
    }

    public String getCompleteTag(String hashtag, String username) {
        try {
            User user = findUser(username);
            return user.getCompleteTag(hashtag);
        } catch (InvalidUserException e) {
            return  null;
        }
    }

    //SETTERS

    //USERS
    /**
     * Ajoute un utilisateur à la liste des utilisateurs du serveur
     * @param user (User) utilisateur
     */
    public void addUser(User user) throws InvalidUserException{
        if (!userExist(user)) users.add(user);
        else throw new InvalidUserException("L'utilisateur est déjà existant");
    }

    /**
     * Ajoute un follower à un utilisateur du serveur
     * @param user (User) utilisateur de la liste des utilisateurs du serveur
     * @param follower (String) follower@domaine
     */
    public void addFollowerToUser(User user, String follower) {
        user.addFollower(follower);
    }

    /**
     * Ajoute un follower à un utilisateur du serveur   !peut changer
     * @param user (User) utilisateur de la liste des utilisateurs du serveur
     * @param tag (String) tag@domaine
     */
    public void addUserTagToUser(User user, String tag) {
        user.addUserTag(tag);
    }


    //TAGS
    /**
     * Ajoute un tag à la liste des tags du serveur
     * @param tag (Tag) tag
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * sauvegarde les messages offline
     * @param messages (Map<String, List<String>>) messages offline
     */
    public void saveMessages (Map<String, List<String>>messages) {
        jsonMessages.saveMessages(messages);
    }

    /**
     * Récupère les messages offline
     * @return (Map<String, List<String>>)
     */
    public Map<String, List<String>> getOfflineMessages () {
        return jsonMessages.getMessages();
    }

    /**
     * Ajoute un message à la liste des messages offline
     * @param completeUsername (String) nom complet de l'utilisateur
     * @param message (String) message
     */
    public void addOfflineMessage(String completeUsername, String message) {
        for (User user : getUsers()) {
            if(completeUsername.contains(user.getUsername())) {
                Map<String, List<String>> messages = jsonMessages.getMessages();
                if (messages == null) {
                    messages = new HashMap<>();
                }
                if (!messages.containsKey(user.getUsername())) {
                    messages.put(user.getUsername(), new ArrayList<>());
                }
                messages.get(user.getUsername()).add(message);
                saveMessages(messages);
                return;
            }
        }
    }


    public boolean isTaskIdDifferent(String follower, String taskId) {
        for (User user: getUsers()) {
            if(follower.contains(user.getUsername())) {
                return user.isTaskIdDifferent(taskId);
            }
        }
        //si utilisateur n'existe alors false
        return false;
    }

    public void updateTaskId(String follower, String taskId) {
        for (User user: getUsers()) {
            if(follower.contains(user.getUsername())) {
                user.addTaskId(taskId);
            }
        }
    }
}
