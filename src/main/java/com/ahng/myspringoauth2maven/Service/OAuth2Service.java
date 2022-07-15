package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.DTO.Kakao;
import com.ahng.myspringoauth2maven.DTO.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private static final Logger log = LogManager.getLogger();

    private final KakaoService kakaoService;

    private final ObjectMapper objectMapper;

    public User verificationKakao(String code) {
        User user = new User();
//        String accessToken = kakaoService.getKakaoAccessTokenByCode(code);
        Kakao kakaoDto = kakaoService.getKakaoAccessTokenByCode(code);
        String userInfo = kakaoService.getKakaoUserInfoByAccessToken(kakaoDto.getAccess_token());

        try {
            JsonNode jsonNode = objectMapper.readTree(userInfo);
            log.warn("JSON Node: " + jsonNode);
            log.warn(String.valueOf(jsonNode.get("kakao_account").get("profile").get("nickname")));
            String nickname = String.valueOf(jsonNode.get("kakao_account").get("profile").get("nickname"));
            log.warn(String.valueOf(jsonNode.get("kakao_account").get("email")));
            String email = String.valueOf(jsonNode.get("kakao_account").get("email"));
            log.warn(String.valueOf(jsonNode.get("kakao_account").get("profile").get("thumbnail_image_url")));
            String picture = String.valueOf(jsonNode.get("kakao_account").get("profile").get("thumbnail_image_url"));
            user.setNickname(nickname);
            user.setEmail(email);
            user.setPicture(picture);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return user;
    }
}
