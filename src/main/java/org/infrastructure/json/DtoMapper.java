package org.infrastructure.json;

import org.infrastructure.dto.ServerDTO;
import org.infrastructure.dto.TagDTO;
import org.infrastructure.dto.UserDTO;
import org.model.ServerSettings;
import org.model.Tag;
import org.model.User;
import org.model.exceptions.InvalidServerSettingsException;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Classe de mapping DTO
 */
public class DtoMapper {

    //User
    /**
     * Converti l'objet user en objet DTO user
     * @param user (User) utilisateur
     * @return (UserDTO) utilisateur dto
     */
    private static UserDTO userToDto(User user) {
        return new UserDTO(user.getUsername(), user.getBcryptRotations(), user.getBcryptSalt(), user.getBcryptHash(), user.getFollowers(), user.getUserTags(), user.getLockoutCounter());
    }

    /**
     * Converti l'objet DTO user en objet user
     * @param userDto (UserDTO) Utilisateur dto
     * @return (User) Utilisateur
     */
    private static User dtoToUser(UserDTO userDto) {
        return new User(userDto.getUsername(), userDto.getBcryptRotations(), userDto.getBcryptSalt(), userDto.getBcryptHash(), userDto.getFollowers(), userDto.getUserTags(), userDto.getLockoutCounter());
    }

    //Tag
    /**
     * Converti l'objet tag en objet DTO tag
     * @param tag (Tag) tag
     * @return (TagDTO) tag dto
     */
    private static TagDTO tagToDto(Tag tag) {
        return new TagDTO(tag.getTag(), tag.getFollowers());
    }

    /**
     * Converti l'objet DTO tag en objet tag
     * @param tagDto (TagDTO) tag dto
     * @return (Tag) tag
     */
    private static Tag dtoToTag(TagDTO tagDto) {
        return new Tag(tagDto.getTag(), tagDto.getFollowers());
    }


    public static ServerSettings dtoToServerSettings(ServerDTO serverDto) throws InvalidServerSettingsException {
        if(serverDto == null) {
            throw new InvalidServerSettingsException("Configuration serveur vide");
        }

        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(serverDto.getBase64AES().getBytes(UTF_8)), "AES");

        List<User> users = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        for (UserDTO userDto: serverDto.getUsers()) { users.add(dtoToUser(userDto)); }
        for (TagDTO tagDto: serverDto.getTags()) { tags.add(dtoToTag(tagDto)); }

        return new ServerSettings(serverDto.getCurrentDomain(), serverDto.getSaltSizeInBytes(), serverDto.getMulticastAddress(), serverDto.getMulticastPort(), serverDto.getUnicastPort(), serverDto.getRelayPort(), serverDto.getNetworkInterface(), keySpec, serverDto.isTls(), users, tags);
    }

    public static ServerDTO SeverSettingsToDto(ServerSettings serverSettings) {
        String AESKey = Base64.getEncoder().encodeToString(serverSettings.getAESKey().getEncoded());

        List<UserDTO> users = new ArrayList<>();
        List<TagDTO> tags = new ArrayList<>();

        for (User user: serverSettings.getUsers()) { users.add(userToDto(user)); }
        for (Tag tag: serverSettings.getTags()) { tags.add(tagToDto(tag)); }

        return new ServerDTO(serverSettings.getCurrentDomain(), serverSettings.getSaltSizeInBytes(), serverSettings.getMulticastAddress(), serverSettings.getMulticastPort(), serverSettings.getUnicastPort(), serverSettings.getRelayPort(), serverSettings.getNetworkInterface(), AESKey, serverSettings.isTls(), users, tags);
    }

}
