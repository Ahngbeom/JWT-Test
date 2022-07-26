package com.ahng.myspringoauth2maven.Controller;

import com.ahng.myspringoauth2maven.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
public class OAuth2Controller {

    private final UserService service;

    public OAuth2Controller(UserService service) {
        this.service = service;
    }

//    @GetMapping("/login/oauth2/code/{provider}")
//    public ResponseEntity<?> oAuth2RequestUser(HttpServletResponse response, @PathVariable String provider, @Valid @RequestBody String code) {
//        log.info(provider);
//        log.info(code);
//
//        return ResponseEntity.ok(code);
//    }
}
