package org.server;

/**
 * Requêtes
 */
public class Queries {

    public static final String HELLO = "HELLO %s %s \r\n";
    public static final String CONNECT = "CONNECT %s \r\n";
    public static final String PARAM = "PARAM %i %s \r\n";
    public static final String CONFIRM = "CONFIRM %s \r\n";
    public static final String REGISTER = "REGISTER %s %i %s \r\n";
    public static final String FOLLOW = "FOLLOW %s \r\n";
    public static final String MSG = "MSG %s \r\n";
    public static final String MSGS = "MSGS %s %s \r\n";
    public static final String OK = "+OK[ %s] \r\n";
    public static final String ERREUR = "-ERR[ %s] \r\n";
    public static final String DISCONNECT = "DISCONNECT \r\n";

}
