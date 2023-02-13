package org.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regexes {

    public static final String DOMAIN = "^[a-zA-Z\\d.]{5,200}$";

    public static final String ADDRESS = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

    public static final String PORT = "^\\d{1,5}$";

    public static final String ROUND_OR_SALT_SIZE = "^\\d{2}$";

    public static final String BCRYPT_HASH = "^[$][2][b][$]\\d{2}[$][a-zA-Z\\d\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60]{1,70}$";

    public static final String SHA3_HEX ="^[a-zA-Z\\d]{30,200}$";

    public static final String BCRYPT_SALT = "^[a-zA-Z\\d\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60]{22}$";

    public static final String MESSAGE = "^[\\x20-\\xFF]{1,200}$";

    public static final String MESSAGE_INTERNE = "^[\\x20-\\xFF]{1,500}$";

    public static final String USERNAME = "^[a-zA-Z\\d]{5,20}$";

    public static final String TAG = "^[#][a-zA-Z\\d]{5,20}$";

    public static final String NAME_DOMAIN = "^[a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}$";

    public static final String TAG_DOMAIN = "^[#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}$";

    public static final String ID_DOMAIN = "^[\\d]{1,5}[@][a-zA-Z\\d.]{5,200}$";


    //Received commands matchers pattern

    public static final Pattern TYPE = Pattern.compile("^(?<type>[A-Z]*)[\\x20]");

    public static final Pattern CONNECT = Pattern.compile("^[C][O][N][N][E][C][T][\\x20](?<username>[a-zA-Z\\d]{5,20})$"); //[\x0D][\x0A]

    public static final Pattern REGISTER = Pattern.compile("^[R][E][G][I][S][T][E][R][\\x20](?<username>[a-zA-Z\\d]{5,20})[\\x20](?<saltsize>\\d{2})[\\x20][$][2][b][$](?<bcryptround>\\d{2})[$](?<bcrypthash>[a-zA-Z\\d\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60]{1,70})$");

    public static final Pattern DISCONNECT = Pattern.compile("^[D][I][S][C][O][N][N][E][C][T][\\x0D][\\x0A]$");

    public static final Pattern CONFIRM = Pattern.compile("^[C][O][N][F][I][R][M][\\x20](?<sha3hex>[a-zA-Z\\d]{30,200})$");

    public static final Pattern MSG = Pattern.compile("^[M][S][G][\\x20](?<message>[\\x20-\\xFF]{1,200})$");

    //(séparé pour reconnaitre les deux séparément)
    public static final Pattern FOLLOW_DOMAIN = Pattern.compile("^[F][O][L][L][O][W][\\x20](?<namedomain>[a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})$");
    public static final Pattern FOLLOW_TAG = Pattern.compile("^[F][O][L][L][O][W][\\x20](?<tagdomain>[#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})$");

    //Multicast
    public static final Pattern ECHO = Pattern.compile("^[E][C][H][O][\\x20](?<port>\\d{1,5})[\\x20](?<domain>[a-zA-Z\\d.]{5,200})");

    //Unicast (séparé pour reconnaitre les deux séparément)
    public static final Pattern SEND_NAME = Pattern.compile("[S][E][N][D][\\x20](?<iddomain>[\\d]{1,5}[@][a-zA-Z\\d.]{5,200})[\\x20](?<namedomain>[a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20](?<namedomain2>[a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20](?<internalmessage>[\\x20-\\xFF]{1,500})");
    public static final Pattern SEND_TAG = Pattern.compile("[S][E][N][D][\\x20](?<iddomain>[\\d]{1,5}[@][a-zA-Z\\d.]{5,200})[\\x20](?<namedomain>[a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20](?<tagdomain>[#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20](?<internalmessage>[\\x20-\\xFF]{1,500})");


    //Decomposers

    /**
     * Décompose la commande REGISTER et récupère les informations sous la forme d'une map
     * @param command (String) Commande REGISTER
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private static Map<String, String> decomposeRegister(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.REGISTER.matcher(command);
        if(m.find()) {
            String bcrypthash = m.group("bcrypthash");
            int saltsize = Integer.parseInt(m.group("saltsize"));

            result.put("username", m.group("username"));
            result.put("bcryptround", m.group("bcryptround"));
            result.put("bcryptsalt", bcrypthash.substring(0, saltsize));
            result.put("bcrypthash", bcrypthash.substring(saltsize));
            System.out.printf("REGISTER : (Username: %s, BcryptRound: %s, BcryptHash: %s, Bcryptsalt: %s)", m.group("username"), m.group("bcryptround"), result.get("bcrypthash"), result.get("bcryptsalt")); //todo debug
        }

        return result;
    }

    /**
     * Décompose la commande CONNECT et récupère les informations sous la forme d'une map
     * @param command (String) Commande CONNECT
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private static Map<String, String> decomposeConnect(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.CONNECT.matcher(command);
        if(m.find()) {
            result.put("username", m.group("username"));

            System.out.printf("CONNECT : (Username: %s)", m.group("username")); //todo debug
        }

        return result;
    }

    /**
     * Décompose la commande CONFIRM et récupère les informations sous la forme d'une map
     * @param command (String) Commande CONFIRM
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private static Map<String, String> decomposeConfirm(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.CONFIRM.matcher(command);
        if(m.find()) {
            result.put("sha3hex", m.group("sha3hex"));

            System.out.printf("CONFIRM : (Sha3hex: %s)", m.group("sha3hex")); //todo debug
        }

        return result;
    }

    /**
     * Décompose la commande MSG et récupère les informations sous la forme d'une map
     * @param command (String) Commande MSG
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private static Map<String, String> decomposeMsg(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.MSG.matcher(command);
        if(m.find()) {
            result.put("message", m.group("message"));

            System.out.printf("MSG : (Message: %s)", m.group("message")); //todo debug
        }

        return result;
    }

    /**
     * Décompose la commande FOLLOW et récupère les informations sous la forme d'une map
     * @param command (String) Commande FOLLOW
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private static Map<String, String> decomposeFollow(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.FOLLOW_DOMAIN.matcher(command);
        if(m.find()) {
            result.put("namedomain", m.group("namedomain"));

            System.out.printf("FOLLOW NAME_DOMAIN : (NameDomain: %s)", m.group("namedomain")); //todo debug
        } else {
            m = Regexes.FOLLOW_TAG.matcher(command);
            if(m.find()) {
                result.put("tagdomain", m.group("tagdomain"));

                System.out.printf("FOLLOW TAG_DOMAIN : (TagDomain: %s)", m.group("tagdomain")); //todo debug
            }
        }

        return result;
    }

    /**
     * Décompose la commande et récupère une Map des valeurs
     * @param command
     * @return
     */
    public static Map<String, String> decomposeCommand(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.TYPE.matcher(command);
        if(m.find()) {
            String type = m.group("type");
            result.put("type", type);

            switch (type) {
                case "REGISTER":
                    result.putAll(decomposeRegister(command));
                    break;

                case "CONNECT":
                    result.putAll(decomposeConnect(command));
                    break;

                case "CONFIRM":
                    result.putAll(decomposeConfirm(command));
                    break;

                case "MSG":
                    result.putAll(decomposeMsg(command));
                    break;

                case "FOLLOW":
                    result.putAll(decomposeFollow(command));
                    break;

            }
        }

        return result;
    }

}