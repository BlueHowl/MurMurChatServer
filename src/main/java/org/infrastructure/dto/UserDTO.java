package org.infrastructure.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Set;

/**
 * Classe DTO Book
 */
public class UserDTO {

    @SerializedName("Username")
    public final String username;

    @SerializedName("BcryptRotations")
    public final int bcryptRotations;

    @SerializedName("BcryptSalt")
    public final String bcryptSalt;

    @SerializedName("BcryptHash")
    public final String bcryptHash;

    @SerializedName("Followers")
    public final Set<String> followers;

    @SerializedName("UserTags")
    public final  Set<String> userTags;

    @SerializedName("LockoutCounter")
    public final  int lockoutCounter;

    @SerializedName("OfflineMessages")
    public final List<String> offlineMessages;

    /**
     * Constructeur utilisateur DTO
     *
     * @param username        (String) nom d'utilisateur
     * @param bcryptRotations (String) rotations bcrypt
     * @param bcryptSalt      (String) salt bcrypt
     * @param bcryptHash      (String) hash bcrypt
     * @param followers       (Set<String>) liste des followers
     * @param userTags        (Set<String>) liste des userTags
     * @param lockoutCounter  (int) lockoutCounter
     */
    public UserDTO(String username, int bcryptRotations, String bcryptSalt, String bcryptHash, Set<String> followers, Set<String> userTags, int lockoutCounter, List<String> offlineMessages) {
        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;
        this.followers = followers;
        this.userTags = userTags;
        this.lockoutCounter = lockoutCounter;
        this.offlineMessages = offlineMessages;
    }

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
        return followers;
    }

    /**
     * Récupère les userTags
     * @return (Set<String>) userTags
     */
    public Set<String> getUserTags() {
        return userTags;
    }

    /**
     * Récupère le lockoutCounter
     * @return (int)
     */
    public int getLockoutCounter() {
        return lockoutCounter;
    }

    /**
     * Récupère les messages hors ligne
     * @return (List<String>)
     */
    public List<String> getOfflineMessages() {
        return offlineMessages;
    }
}
