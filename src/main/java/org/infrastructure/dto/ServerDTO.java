package org.infrastructure.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerDTO {

    @SerializedName("CurrentDomain")
    public final String currentDomain;

    @SerializedName("SaltSizeInBytes")
    public final int saltSizeInBytes;

    @SerializedName("MulticastAddress")
    public final String multicastAddress;

    @SerializedName("MulticastPort")
    public final int multicastPort;

    @SerializedName("UnicastPort")
    public final int unicastPort;

    @SerializedName("RelayPort")
    public final int relayPort;

    @SerializedName("NetworkInterface")
    public final String networkInterface;

    @SerializedName("Base64AES")
    public final String base64AES;

    @SerializedName("Tls")
    public final boolean tls;

    @SerializedName("Users")
    public final List<UserDTO> users;

    @SerializedName("Tags")
    public final List<TagDTO> tags;

    public ServerDTO(String currentDomain, int saltSizeInBytes, String multicastAddress, int multicastPort, int unicastPort, int relayPort, String networkInterface, String base64AES, boolean tls, List<UserDTO> users, List<TagDTO> tags) {
        this.currentDomain = currentDomain;
        this.saltSizeInBytes = saltSizeInBytes;
        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        this.unicastPort = unicastPort;
        this.relayPort = relayPort;
        this.networkInterface = networkInterface;
        this.base64AES = base64AES;
        this.tls = tls;
        this.users = users;
        this.tags = tags;
    }

    /**
     * Récupère le nom de domaine du serveur
     * @return (String)
     */
    public String getCurrentDomain() {
        return currentDomain;
    }

    /**
     * Récupère la taille de Salt
     * @return
     */
    public int getSaltSizeInBytes() {
        return saltSizeInBytes;
    }

    /**
     * Récupère l'adresse de multicast
     * @return
     */
    public String getMulticastAddress() {
        return multicastAddress;
    }

    /**
     * Récupère le port multicast
     * @return (int)
     */
    public int getMulticastPort() {
        return multicastPort;
    }

    /**
     * Récupère le port unicast
     * @return (int)
     */
    public int getUnicastPort() {
        return unicastPort;
    }

    /**
     * Récupère le port du relai
     * @return (int)
     */
    public int getRelayPort() {
        return relayPort;
    }

    /**
     * Récupère l'interface réseaux à utiliser
     * @return (String)
     */
    public String getNetworkInterface() {
        return networkInterface;
    }

    /**
     * Récupère la clé AES en base 64
     * @return (String)
     */
    public String getBase64AES() {
        return base64AES;
    }

    /**
     * Récupère la valeur Tls
     * @return (boolean)
     */
    public boolean isTls() {
        return tls;
    }

    /**
     * Récupère la liste des utilisateurs DTO
     * @return (UserDTO)
     */
    public List<UserDTO> getUsers() {
        return users;
    }

    /**
     * Récupère la liste des tags DTO
     * @return (TagDTO)
     */
    public List<TagDTO> getTags() {
        return tags;
    }
}
