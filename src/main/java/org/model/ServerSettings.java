package org.model;

import org.infrastructure.dto.TagDTO;
import org.infrastructure.dto.UserDTO;
import org.model.exceptions.InvalidServerSettingsException;
import org.model.exceptions.InvalidTagException;
import org.model.exceptions.InvalidUserException;
import org.utils.Regexes;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.regex.Pattern;

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

    public ServerSettings(String currentDomain, int saltSizeInBytes, String multicastAddress, int multicastPort, int unicastPort, int relayPort, String networkInterface, SecretKeySpec AESKey, boolean tls, List<User> users, List<Tag> tags) throws InvalidServerSettingsException {
        checkParameters(currentDomain, saltSizeInBytes, multicastAddress, multicastPort, unicastPort, relayPort, networkInterface, AESKey, tls, users, tags);

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

    private void checkParameters(String currentDomain, int saltSizeInBytes, String multicastAddress, int multicastPort, int unicastPort, int relayPort, String networkInterface, SecretKeySpec aesKey, boolean tls, List<User> users, List<Tag> tags) throws InvalidServerSettingsException{
        //todo verifier networkInterface ?
        if(!Pattern.matches(Regexes.DOMAIN, currentDomain) ||
                !Pattern.matches(Regexes.ROUND_OR_SALT_SIZE, String.valueOf(saltSizeInBytes)) ||
                !Pattern.matches(Regexes.ADDRESS, multicastAddress) ||
                !Pattern.matches(Regexes.PORT, String.valueOf(multicastPort)) ||
                !Pattern.matches(Regexes.PORT, String.valueOf(unicastPort)) ||
                !Pattern.matches(Regexes.PORT, String.valueOf(relayPort)))
        {
            throw new InvalidServerSettingsException("Valeurs paramètres serveur invalides");
        }
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
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Ajoute un follower à un utilisateur du serveur   !peut changer
     * @param user (User) utilisateur de la liste des utilisateurs du serveur
     * @param follower (String) follower@domaine
     * @throws InvalidUserException Exception lancée si le champ follower ne respecte pas la syntaxe
     */
    public void addFollowerToUser(User user, String follower) throws InvalidUserException {
        user.addFollower(follower);
    }

    /**
     * Ajoute un follower à un utilisateur du serveur   !peut changer
     * @param user (User) utilisateur de la liste des utilisateurs du serveur
     * @param tag (String) tag@domaine
     * @throws InvalidUserException Exception lancée si le champ tag ne respecte pas la syntaxe
     */
    public void addUserTagToUser(User user, String tag) throws InvalidUserException {
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
     * @throws InvalidTagException Exception lancée si le champ follower ne respecte pas la syntaxe
     */
    public void addFollowerToTag(Tag tag, String follower) throws InvalidTagException {
        tag.addFollower(follower);
    }


}
