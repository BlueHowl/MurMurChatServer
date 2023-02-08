package org.infrastructure.dto;

import com.google.gson.annotations.SerializedName;

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

    /**
     * Constructeur utilisateur DTO
     * @param username (String) nom d'utilisateur
     * @param bcryptRotations (String) rotations bcrypt
     * @param bcryptSalt (String) salt bcrypt
     * @param bcryptHash (String) hash bcrypt
     */
    public UserDTO(String username, int bcryptRotations, String bcryptSalt, String bcryptHash) {
        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;
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
}
