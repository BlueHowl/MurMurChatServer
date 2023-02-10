package org.infrastructure.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Classe tag dto
 */
public class TagDTO {

    @SerializedName("Tag")
    public final String tag;

    @SerializedName("Followers")
    public final List<String> followers;

    public TagDTO(String tag, List<String> followers) {
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
     * @return (List<String>) liste de followers
     */
    public List<String> getFollowers() {
        return followers;
    }
}
