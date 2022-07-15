package com.ahng.myspringoauth2maven.Component;

import com.ahng.myspringoauth2maven.DTO.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRequestMapper {

    public User toDTO(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return User.builder()
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("name"))
                .picture((String) attributes.get("picture"))
                .build();
    }

//    public UserFindRequest toFindDto(UserDto userDto) {
//        return new UserFindRequest(userDto.getEmail());
//    }
}
