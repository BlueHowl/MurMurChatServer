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

}