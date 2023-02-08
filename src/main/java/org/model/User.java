package org.model;

/**
 * Data class de mémorisation des informations utilisateur
 */
public class User {
    private String username;

    private int bcryptRotations;

    private String bcryptSalt;

    private String bcryptHash;

    /**
     * Constructeur de la classe user
     */
    public User(String username, int bcryptRotations, String bcryptSalt, String bcryptHash) {
        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;

        //TODO REGEX vérif validité données
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
