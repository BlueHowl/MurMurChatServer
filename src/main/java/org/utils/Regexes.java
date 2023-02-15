package org.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regexes {

    private static Regexes instance;

    private static final Pattern TYPE = Pattern.compile("^(?<type>[A-Z]*)[\\x20]");

    private static final Pattern CONNECT = Pattern.compile("^[C][O][N][N][E][C][T][\\x20](?<username>[a-zA-Z\\d]{5,20})$"); //[\x0D][\x0A]

    private static final Pattern REGISTER = Pattern.compile("^REGISTER[\\x20](?<username>[a-zA-Z\\d]{5,20})[\\x20](?<saltsize>\\d{2})[\\x20][$][2][b][$](?<bcryptround>\\d{2})[$](?<bcrypthash>[a-zA-Z\\d\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60]{1,70})$");

    private static final Pattern CONFIRM = Pattern.compile("^CONFIRM[\\x20](?<sha3hex>[a-zA-Z\\d]{30,200})$");

    private static final Pattern MSG = Pattern.compile("^MSG[\\x20](?<message>[\\x20-\\xFF]{1,200})$");

    private static final Pattern FOLLOW = Pattern.compile("^FOLLOW[\\x20]((?<tag>[#][a-zA-Z\\d]{5,20})|(?<name>[a-zA-Z\\d]{5,20}))[@](?<domain>[a-zA-Z\\d.]{5,200})$");

    private static final Pattern HASHTAG = Pattern.compile("(#[a-zA-Z\\d]{5,20})");

    //Decomposers

    /**
     * Décompose la commande REGISTER et récupère les informations sous la forme d'une map
     * @param command (String) Commande REGISTER
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private Map<String, String> decomposeRegister(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.REGISTER.matcher(command);
        if(m.find()) {
            String bcrypthash = m.group("bcrypthash");
            int saltsize = Integer.parseInt(m.group("saltsize"));

            result.put("username", m.group("username"));
            result.put("bcryptround", m.group("bcryptround"));
            result.put("bcryptsalt", bcrypthash.substring(0, saltsize));
            result.put("bcrypthash", bcrypthash.substring(saltsize));
            System.out.printf("REGISTER : (Username: %s, BcryptRound: %s, BcryptHash: %s, Bcryptsalt: %s)\n", m.group("username"), m.group("bcryptround"), result.get("bcrypthash"), result.get("bcryptsalt")); //todo debug
        }

        return result;
    }

    /**
     * Décompose la commande CONNECT et récupère les informations sous la forme d'une map
     * @param command (String) Commande CONNECT
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private Map<String, String> decomposeConnect(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.CONNECT.matcher(command);
        if(m.find()) {
            result.put("username", m.group("username"));

            System.out.printf("CONNECT : (Username: %s)\n", m.group("username")); //todo debug
        }

        return result;
    }

    /**
     * Décompose la commande CONFIRM et récupère les informations sous la forme d'une map
     * @param command (String) Commande CONFIRM
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private Map<String, String> decomposeConfirm(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.CONFIRM.matcher(command);
        if(m.find()) {
            result.put("sha3hex", m.group("sha3hex"));

            System.out.printf("CONFIRM : (Sha3hex: %s)\n", m.group("sha3hex")); //todo debug
        }

        return result;
    }

    /**
     * Décompose la commande MSG et récupère les informations sous la forme d'une map
     * @param command (String) Commande MSG
     * @return (Map<String, String>) Map des informations de la commande, vide si syntaxe invalide
     */
    private Map<String, String> decomposeMsg(String command) {
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
    private Map<String, String> decomposeFollow(String command) {
        Map<String, String> result = new HashMap<>();

        Matcher m = Regexes.FOLLOW.matcher(command);
        if(m.find()) {
            result.put("name", m.group("name"));
            result.put("tag", m.group("tag"));
            result.put("domain", m.group("domain"));

            System.out.printf("FOLLOW : (Name: %s), (Tag: %s), (Domain: %s)", m.group("name"), m.group("tag"), m.group("domain")); //todo debug
        }
        return result;
    }

    /**
     * Décompose un message et récupère les hashtags
     * @param message (String)
     * @return String[] tableau de hashtag
     */
    public String[] decomposeHashtags(String message) {
        String[] hashtag = {};

        Matcher m = Regexes.HASHTAG.matcher(message);
        if(m.find()) {
            hashtag = new String[m.groupCount()];

            for(int i = 0; i < m.groupCount(); ++i) {
                hashtag[i] = m.group(i);
            }

            return hashtag;
        }

        return hashtag;
    }

    /**
     * Décompose la commande et récupère une Map des valeurs
     * @param command (String)
     * @return (Map<String, String>) valeurs de la commande décomposée
     */
    public Map<String, String> decomposeCommand(String command) {
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

    public static Regexes getInstance() {
        if (instance == null) {
            instance = new Regexes();
        }
        return instance;
    }

}