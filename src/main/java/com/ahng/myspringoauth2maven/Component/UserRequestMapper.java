package com.ahng.myspringoauth2maven.Component;

import com.ahng.myspringoauth2maven.DTO.OAuth2UserDTO;
import com.ahng.myspringoauth2maven.Entity.Authority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class UserRequestMapper {

    public OAuth2UserDTO toDto(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return OAuth2UserDTO.builder()
                .email((String)attributes.get("email"))
                .name((String)attributes.get("name"))
                .picture((String)attributes.get("picture"))
                .build();
    }

}
