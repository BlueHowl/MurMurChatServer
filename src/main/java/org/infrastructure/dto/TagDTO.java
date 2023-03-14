package org.infrastructure.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

/**
 * Classe tag dto
 */
public class TagDTO {

    @SerializedName("Tag")
    public final String tag;

    @SerializedName("Followers")
    public final Set<String> followers;

    /**
     * Constructeur classe tag dto
     * @param tag (String) nom du tag
     * @param followers (Set<String>) liste des followers
     */
    public TagDTO(String tag, Set<String> followers) {
        this.tag = tag;
        this.followers = followers;
    }

    /**
     * Récupère le nom du tag
     * @return (String) nom
     */
    public String getTag() {
        return tag;
    }

    /**
     * Récupère les followers
     * @return (Set<String>) liste de followers
     */
    public Set<String> getFollowers() {
        return followers;
    }
}
