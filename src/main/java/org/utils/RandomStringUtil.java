package org.utils;

import java.util.Date;
import java.util.Random;

/**
 * Classe utile permettant de générer un String aléatoire contenant les caractères autorisés
 */
public class RandomStringUtil {
    private static final String caracters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#%&'()*,-./:;<=>?@[]^_`";

    /**
     * Génère une chaine de caractères aléatoire de la taille donnée et contenant les caractères autorisés
     * @param length (int) Taille de la chaine à retourner
     * @return (String) chaine de caractères aléatoire
     */
    public static String generateString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        random.setSeed(new Date().getTime());

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(caracters.length());

            sb.append(caracters.charAt(index));
        }

        return sb.toString();
    }

}
