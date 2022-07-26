package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.DTO.OAuth2Attribute;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

//        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

//        log.info("{}", oAuth2Attribute);

//        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        log.info(registrationId);
        log.info(userNameAttributeName);

        oAuth2User.getAttributes().forEach((s, o) -> {
            try {
                log.info(s + ": " + o.toString());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });

        Map<String, Object> memberAttribute = new HashMap<>();
        memberAttribute.put("id", oAuth2User.getAttributes().get("id"));
        memberAttribute.put("email",  oAuth2User.getAttributes().get("email"));
        memberAttribute.put("name",  oAuth2User.getAttributes().get("name"));
        memberAttribute.put("picture", oAuth2User.getAttributes().get("avatar_url"));
        memberAttribute.put("attributes", oAuth2User.getAttributes());
        memberAttribute.put("attributesKey", "id");

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_OAUTH2")), memberAttribute, "email");
    }
}

