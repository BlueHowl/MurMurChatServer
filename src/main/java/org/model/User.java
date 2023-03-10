package org.model;

import java.util.*;

/**
 * Data class de mémorisation des informations utilisateur
 */
public class User {
    private final String username;

    private final int bcryptRotations;

    private final String bcryptSalt;

    private final String bcryptHash;

    private Set<String> followers;

    private Set<String> userTags;

    private int lockoutCounter;

    private List<String> offlineMessages;

    /**
     * Constructeur classe utilisateur
     *
     * @param username        (String) nom d'utilisateur
     * @param bcryptRotations (String) rotations bcrypt
     * @param bcryptSalt      (String) salt bcrypt
     * @param bcryptHash      (String) hash bcrypt
     * @param followers       (Set<String>) liste des followers
     * @param userTags        (Set<String>) liste des userTags
     * @param lockoutCounter  (int) lockoutCounter // pas utilisé
     * @param offlineMessages (List<String>) liste des messages offline
     */
    public User(String username, int bcryptRotations, String bcryptSalt, String bcryptHash, Set<String> followers, Set<String> userTags, int lockoutCounter, List<String> offlineMessages) {
        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;
        this.followers = (followers != null) ? followers : new HashSet<>();
        this.userTags = (userTags != null) ? userTags : new HashSet<>();
        this.lockoutCounter = lockoutCounter;
        this.offlineMessages = (offlineMessages != null) ? offlineMessages : new ArrayList<>();
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
     * @return (Set<String>) followers
     */
    public Set<String> getFollowers() {
        return new HashSet<>(followers);
    }

    /**
     * Récupère les userTags
     * @return (Set<String>) userTags
     */
    public Set<String> getUserTags() {
        return new HashSet<>(userTags);
    }

    /**
     * Récupère le tag complet (tag@domain)
     * @param hashtag (String) tag sans @domain
     * @return String tag complet
     */
    public String getCompleteTag(String hashtag) {
        for (String tag : userTags) {
            if(tag.contains(hashtag)) {
                return tag;
            }
        }

        return null;
    }

    /**
     * Récupère le lockoutCounter
     * @return (int)
     */
    public int getLockoutCounter() {
        return lockoutCounter;
    }

    /**
     * Récupère la liste des messages offline
     * @return (List<String>) offlineMessages
     */
    public List<String> getOfflineMessages() {
        return offlineMessages;
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

    /**
     * Ajoute un message à la liste des messages offline
     * @param message (String) message
     */
    public void addOfflineMessage(String message) {
        offlineMessages.add(message);
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
