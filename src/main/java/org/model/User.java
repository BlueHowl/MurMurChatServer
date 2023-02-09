package org.model;

import org.model.exceptions.UserNotValidException;
import org.utils.Regexes;

import java.util.regex.Pattern;

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
    public User(String username, int bcryptRotations, String bcryptSalt, String bcryptHash) throws UserNotValidException {
        checkParameters(username, bcryptRotations, bcryptSalt, bcryptHash);

        this.username = username;
        this.bcryptRotations = bcryptRotations;
        this.bcryptSalt = bcryptSalt;
        this.bcryptHash = bcryptHash;
    }

    /**
     * Méthode qui vérifie les paramètres avec des regex
     * @param username (String) nom d'utilisateur
     * @param bcryptRotations (int) rotations bcrypt
     * @param bcryptSalt (String) Sel bcrypt
     * @param bcryptHash (String) hash bcrypt
     * @throws UserNotValidException Exception lancée si une des valeurs ne respecte pas sa syntaxe
     */
    private void checkParameters(String username, int bcryptRotations, String bcryptSalt, String bcryptHash) throws UserNotValidException {
        if(!Pattern.matches(Regexes.USERNAME, username) ||
            !Pattern.matches(Regexes.ROUND_OR_SALT_SIZE, String.valueOf(bcryptRotations)) ||
            !Pattern.matches(Regexes.BCRYPT_SALT, bcryptSalt) ||
            !Pattern.matches(Regexes.BCRYPT_HASH, bcryptHash))
        {
            throw new UserNotValidException("Valeurs utilisateur invalides");
        }
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
