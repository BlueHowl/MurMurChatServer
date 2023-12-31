package org.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Classe tag dto
 */
public class Tag {

    private final String name;
    private final Set<String> followers;

    /**
     * Constructeur général classe tag
     * @param name (String) nom du tag
     * @param followers (Set<String>) liste des followers
     */
    public Tag(String name, Set<String> followers) {
        this.name = name;
        this.followers = (followers != null) ? followers : new HashSet<>();
    }

    /**
     * Constructeur classe tag
     * @param name (String) nom du tag
     */
    public Tag(String name) {
        this(name, null);
    }


    //GETTERS

    /**
     * Récupère le nom du tag
     * @return (String) nom
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère les followers
     * @return (Set<String>) liste de followers
     */
    public Set<String> getFollowers() {
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
        return t.getName().equals(((Tag) o).getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

