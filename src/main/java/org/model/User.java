package org.model;

import java.util.List;
import java.util.Objects;

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
    public User(String username, int bcryptRotations, String bcryptSalt, String bcryptHash, List<String> followers, List<String> userTags, int lockoutCounter) {
        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;
        this.followers = followers;
        this.userTags = userTags;
        this.lockoutCounter = lockoutCounter;
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
     */
    public void addFollower(String follower) {
        followers.add(follower);
    }

    /**
     * Ajoute un tag à la liste des userTags de l'utilisateur
     * @param tag (String) tag
     * */
    public void addUserTag(String tag) {
        userTags.add(tag);
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
