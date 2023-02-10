package org.utils;

import java.util.regex.Pattern;

public class Regexes {

    public static final String DOMAIN = "^[a-zA-Z\\d.]{5,200}$";

    public static final String ADDRESS = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

    public static final String PORT = "^\\d{1,5}$";

    public static final String ROUND_OR_SALT_SIZE = "^\\d{2}$";

    public static final String BCRYPT_HASH = "^[2][b][$]\\d{2}[$][a-zA-Z\\d\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60]{1,70}$";

    public static final String SHA3_HEX ="^[a-zA-Z\\d]{30,200}$";

    public static final String BCRYPT_SALT = "^[a-zA-Z\\d\\x21-\\x2F\\x3A\\x-40\\x5B-\\x60]{22}$";

    public static final String MESSAGE = "^[\\x20-\\xFF]{1,200}$";

    public static final String MESSAGE_INTERNE = "^[\\x20-\\xFF]{1,500}$";

    public static final String USERNAME = "^[a-zA-Z\\d]{5,20}$";

    public static final String TAG = "^[#][a-zA-Z\\d]{5,20}$";

    public static final String NAME_DOMAIN = "^[a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}$";

    public static final String TAG_DOMAIN = "^[#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}$";

    public static final String ID_DOMAIN = "^[\\d]{1,5}[@][a-zA-Z\\d.]{5,200}$";


    //Received commands matchers pattern

    public static final Pattern CONNECT = Pattern.compile("^[C][O][N][N][E][C][T][\\x20]([a-zA-Z\\d]{5,20})[\\x0D][\\x0A]$");

    public static final Pattern REGISTER = Pattern.compile("^[R][E][G][I][S][T][E][R][\\x20]([a-zA-Z\\d]{5,20})[\\x20](\\d{2})[\\x20][2][b][$](\\d{2})[$]([a-zA-Z\\d\\x21-\\x2F\\x3A-\\x40\\x5B-\\x60]{1,70})[\\x0D][\\x0A]$");

    public static final Pattern DISCONNECT = Pattern.compile("^[D][I][S][C][O][N][N][E][C][T][\\x0D][\\x0A]$");

    public static final Pattern CONFIRM = Pattern.compile("^[C][O][N][F][I][R][M][\\x20]([a-zA-Z\\d]{30,200})[\\x0D][\\x0A]$");

    public static final Pattern MSG = Pattern.compile("^[M][S][G][\\x1F]([\\x20-\\xFF]{1,200})[\\x0D][\\x0A]$");

    public static final Pattern FOLLOW_DOMAIN = Pattern.compile("^[F][O][L][L][O][W][\\x20]([a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x0D][\\x0A]$");

    public static final Pattern FOLLOW_TAG = Pattern.compile("^[F][O][L][L][O][W][\\x20]([#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x0D][\\x0A]$");

    //Multicast
    public static final Pattern ECHO = Pattern.compile("^[E][C][H][O][\\x20](\\d{1,5})[\\x20]([a-zA-Z\\d.]{5,200})[\\x0D][\\x0A]");

    //Unicast
    public static final Pattern SEND_NAME = Pattern.compile("[S][E][N][D][\\x20]([\\d]{1,5}[@][a-zA-Z\\d.]{5,200})[\\x20]([a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20]([a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20]([\\x20-\\xFF]{1,500})[\\x0D][\\x0A]");

    public static final Pattern SEND_TAG = Pattern.compile("[S][E][N][D][\\x20]([\\d]{1,5}[@][a-zA-Z\\d.]{5,200})[\\x20]([a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20]([#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200})[\\x20]([\\x20-\\xFF]{1,500})[\\x0D][\\x0A]");

}