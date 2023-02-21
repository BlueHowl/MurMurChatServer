package org.model;

import org.model.exceptions.InvalidTagException;
import org.model.exceptions.InvalidUserException;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.Optional;

/**
 * Classe de stockage des paramètres serveurs et des utilisateurs et tags
 */
public class ServerSettings {
    //todo enlever les param serveur si directement assignables  + renommer Server et changer l'ancienne classe serveur ?
    private final String currentDomain;
    private final int saltSizeInBytes;
    private final String multicastAddress;
    private final int multicastPort;
    private final int unicastPort;
    private final int relayPort;
    private final String networkInterface;
    private final SecretKeySpec AESKey;
    private final boolean tls;
    private final List<User> users;
    private final List<Tag> tags;

    public ServerSettings(String currentDomain, int saltSizeInBytes, String multicastAddress, int multicastPort, int unicastPort, int relayPort, String networkInterface, SecretKeySpec AESKey, boolean tls, List<User> users, List<Tag> tags) {
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
     * @return
     */
    public int getSaltSizeInBytes() {
        return saltSizeInBytes;
    }

    /**
     * Récupère l'adresse de multicast
     * @return
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
    public SecretKeySpec getAESKey() {
        return AESKey;
    }

    /**
     * Récupère la valeur Tls
     * @return (boolean)
     */
    public boolean isTls() {
        return tls;
    }

    /**
     * Récupère la liste des utilisateurs DTO
     * @return (UserDTO)
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Récupère la liste des tags DTO
     * @return (TagDTO)
     */
    public List<Tag> getTags() {
        return tags;
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

    /**
     * Ajoute un follower à un utilisateur du serveur
     * @param user (User) utilisateur de la liste des utilisateurs du serveur
     * @param follower (String) follower@domaine
     */
    public void addFollowerToUser(User user, String follower) {
        user.addFollower(follower);
    }

    public boolean tagExists(String tag) {
        return tags.stream().anyMatch(t -> t.getTag().equals(tag));
    }

    public Tag findTag(String tag) throws InvalidTagException {
        Optional<Tag> correctTag = tags.stream().filter(t -> t.getTag().equals(tag)).findFirst();
        if (correctTag.isPresent())
            return correctTag.get();
        else throw new InvalidTagException("Le tag n'existe pas");
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
     * Ajoute un follower à un tag du serveur   !peut changer
     * @param tag (Tag) tag de la liste des tags du serveur
     * @param follower (String) follower@domaine
     */
    public void addFollowerToTag(Tag tag, String follower) {
        tag.addFollower(follower);
    }


}