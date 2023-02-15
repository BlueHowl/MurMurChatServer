package org.model;

import java.util.List;
import java.util.Objects;

/**
 * Classe tag dto
 */
public class Tag {

    private final String tag;
    private final List<String> followers;

    public Tag(String tag, List<String> followers) {
        this.tag = tag;
        this.followers = followers;
    }


    //GETTERS

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


    //SETTERS

    /**
     * Ajoute un follower à la liste des followers du tag
     * @param follower (String) follower
     */
    public void addFollower(String follower) {
        followers.add(follower);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag t = (Tag) o;
        return t.getTag().equals(((Tag) o).getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }
}

