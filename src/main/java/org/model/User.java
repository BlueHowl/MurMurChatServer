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

    private final Set<String> followers;

    private final Set<String> userTags;

    private int lockoutCounter;

    private List<String> offlineMessages;

    private final List<String> taskIds = new ArrayList<>();

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
     */
    public User(String username, int bcryptRotations, String bcryptSalt, String bcryptHash, Set<String> followers, Set<String> userTags, int lockoutCounter) {
        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;
        this.followers = (followers != null) ? followers : new HashSet<>();
        this.userTags = (userTags != null) ? userTags : new HashSet<>();
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

    public boolean isTaskIdDifferent(String taskId) {
        return !taskIds.contains(taskId);
    }

    public void addTaskId(String taskId) {
        taskIds.add(taskId);
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
