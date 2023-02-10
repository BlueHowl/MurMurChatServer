package org.utils;

public class Regexes {

    public static final String DOMAIN = "^[a-zA-Z\\d.]{5,200}$";

    public static final String PORT = "^\\d{1,5}$";

    public static final String ROUND_OR_SALT_SIZE = "^\\d{2}$";

    public static final String BCRYPT_HASH = "^[2][b][\\$]\\d{2}[a-zA-Z\\d\\x21-\\x2F\\x3A\\x-40\\x5B-\\x60]{1,70}$";

    public static final String SHA3_HEX ="^[a-zA-Z\\d]{30,200}$";

    public static final String BCRYPT_SALT = "^[a-zA-Z\\d\\x21-\\x2F\\x3A\\x-40\\x5B-\\x60]{22}$";

    public static final String MESSAGE = "^[\\x20-\\xFF]{1,200}$";

    public static final String MESSAGE_INTERNE = "^[\\x20-\\xFF]{1,500}$";

    public static final String USERNAME = "^[a-zA-Z\\d]{5,20}$";

    public static final String TAG = "^[#][a-zA-Z\\d]{5,20}$";

    public static final String DOMAIN_NAME = "^[a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}$";

    public static final String TAG_DOMAIN = "^[#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}$";

    public static final String ID_DOMAIN = "^[\\d]{1,5}[@][a-zA-Z\\d.]{5,200}$";


    //Received commands from clients

    public static final String CONNECT = "^[C][O][N][N][E][C][T][\\x20][a-zA-Z\\d]{5,20}[\\xD][\\xA]$";

    public static final String REGISTER = "^[R][E][G][I][S][T][E][R][\\x20][a-zA-Z\\d]{5,20}[\\x20]\\d{2}[\\x20][2][b][\\$]\\d{2}[a-zA-Z\\d\\x21-\\x2F\\x3A\\x-40\\x5B-\\x60]{1,70}[\\xD][\\xA]$";

    public static final String DISCONNECT = "^[D][I][S][C][O][N][N][E][C][T][\\xD][\\xA]$";

    public static final String CONFIRM = "^[C][O][N][F][I][R][M][\\x20][a-zA-Z\\d]{30,200}[\\xD][\\xA]$";

    public static final String MSG = "^[M][S][G][\\x1F][\\x20-\\xFF]{1,200}[\\xD][\\xA]$";

    public static final String FOLLOW_DOMAIN = "^[F][O][L][L][O][W][\\x20][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}[\\xD][\\xA]$";

    public static final String FOLLOW_TAG = "^[F][O][L][L][O][W][\\x20][#][a-zA-Z\\d]{5,20}[@][a-zA-Z\\d.]{5,200}[\\xD][\\xA]$";

}