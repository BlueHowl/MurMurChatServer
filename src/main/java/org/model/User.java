package org.model;

import org.model.exceptions.InvalidTagException;
import org.model.exceptions.InvalidUserException;
import org.utils.Regexes;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Data class de mémorisation des informations utilisateur
 */
public class User {
    private final String username;

    private final int bcryptRotations;

    private final String bcryptSalt;

    private final String bcryptHash;

    private List<String> followers;

    private List<String> userTags;

    private int lockoutCounter;

    /**
     * Constructeur classe utilisateur
     *
     * @param username        (String) nom d'utilisateur
     * @param bcryptRotations (String) rotations bcrypt
     * @param bcryptSalt      (String) salt bcrypt
     * @param bcryptHash      (String) hash bcrypt
     * @param followers       (List<String>) liste des followers
     * @param userTags        (List<String>) liste des userTags
     * @param lockoutCounter  (int) lockoutCounter
     */
    public User(String username, int bcryptRotations, String bcryptSalt, String bcryptHash, List<String> followers, List<String> userTags, int lockoutCounter) throws InvalidUserException {
        checkParameters(username, bcryptRotations, bcryptSalt, bcryptHash);

        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;
        this.followers = followers;
        this.userTags = userTags;
        this.lockoutCounter = lockoutCounter;
    }

    /**
     * Méthode qui vérifie les paramètres avec des regex
     * @param username (String) nom d'utilisateur
     * @param bcryptRotations (int) rotations bcrypt
     * @param bcryptSalt (String) Sel bcrypt
     * @param bcryptHash (String) hash bcrypt
     * @throws InvalidUserException Exception lancée si une des valeurs ne respecte pas sa syntaxe
     */
    private void checkParameters(String username, int bcryptRotations, String bcryptSalt, String bcryptHash) throws InvalidUserException {
        if(!Pattern.matches(Regexes.USERNAME, username) ||
                !Pattern.matches(Regexes.ROUND_OR_SALT_SIZE, String.valueOf(bcryptRotations)) ||
                !Pattern.matches(Regexes.BCRYPT_SALT, bcryptSalt) ||
                !Pattern.matches(Regexes.BCRYPT_HASH, bcryptHash))
        {
            throw new InvalidUserException("Valeurs utilisateur invalides");
        }
    }


    //GETTERS

    /**
     * Récupère le nom d'utilisateur
     * @return (String) username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Récupère le nombre de rotations bcrypt
     * @return (int) bcryptRotations
     */
    public int getBcryptRotations() {
        return bcryptRotations;
    }

    /**
     * Récupère le salt bcrypt
     * @return (String) bcryptSalt
     */
    public String getBcryptSalt() {
        return bcryptSalt;
    }

    /**
     * Récupère le hash bcrypt
     * @return (String) bcryptHash
     */
    public String getBcryptHash() {
        return bcryptHash;
    }

    /**
     * Récupère les followers
     * @return (List<String>) followers
     */
    public List<String> getFollowers() {
        return followers;
    }

    /**
     * Récupère les userTags
     * @return (List<String>) userTags
     */
    public List<String> getUserTags() {
        return userTags;
    }

    /**
     * Récupère le lockoutCounter
     * @return (int)
     */
    public int getLockoutCounter() {
        return lockoutCounter;
    }


    //SETTERS

    /**
     * Ajoute un follower à la liste des followers du tag
     * @param follower (String) follower
     * @throws InvalidUserException Exception lancée lorsque le champ follower ne respecte pas l syntaxe
     */
    public void addFollower(String follower) throws InvalidUserException {
        if(Pattern.matches(Regexes.NAME_DOMAIN, follower))
        {
            followers.add(follower);
        } else {
            throw new InvalidUserException("La valeur follower ne respecte pas la syntaxe NAME_DOMAIN");
        }
    }

    /**
     * Ajoute un tag à la liste des userTags de l'utilisateur
     * @param tag (String) tag
     * @throws InvalidUserException Exception lancée lorsque le champ follower ne respecte pas l syntaxe
     */
    public void addUserTag(String tag) throws InvalidTagException {
        if(Pattern.matches(Regexes.TAG_DOMAIN, tag))
        {
            userTags.add(tag);
        } else {
            throw new InvalidTagException("La valeur tag ne respecte pas la syntaxe TAG_DOMAIN");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
