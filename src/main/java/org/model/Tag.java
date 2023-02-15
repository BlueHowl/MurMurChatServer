package org.model;

import org.model.exceptions.InvalidTagException;
import org.utils.Regexes;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Classe tag dto
 */
public class Tag {

    private final String tag;
    private final List<String> followers;

    public Tag(String tag, List<String> followers) throws InvalidTagException {
        checkParameters(tag, followers);

        this.tag = tag;
        this.followers = followers;
    }

    private void checkParameters(String tag, List<String> followers) throws InvalidTagException {
        if(!Pattern.matches(Regexes.TAG, tag) || !checkFollowersParameter(followers))
        {
            throw new InvalidTagException("Valeurs tag invalides");
        }
    }

    private boolean checkFollowersParameter(List<String> followers) {
        for (String follower : followers) {
            if(!Pattern.matches(Regexes.NAME_DOMAIN, follower))
                return false;
        }
        return true;
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
     * @throws InvalidTagException Exception lancée lorsque le champ follower ne respecte pas l syntaxe
     */
    public void addFollower(String follower) throws InvalidTagException {
        if(Pattern.matches(Regexes.NAME_DOMAIN, follower))
        {
            followers.add(follower);
        } else {
            throw new InvalidTagException("La valeurs follower ne respecte pas la syntaxe NAME_DOMAIN");
        }
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

