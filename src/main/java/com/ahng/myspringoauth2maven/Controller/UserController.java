package com.ahng.myspringoauth2maven.Controller;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.Domain.UserEntity;
import com.ahng.myspringoauth2maven.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LogManager.getLogger();
    private final UserService service;

//    @GetMapping("/user")
//    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
//        return Collections.singletonMap("name", principal.getAttribute("name"));
//    }

    @GetMapping("/user")
    public User user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null)
            return null;
        principal.getAttributes().forEach((s, o) -> log.warn(s + ": " + o));
        User user = new User();
        user.setNickname(principal.getAttribute("name"));
        user.setEmail(principal.getAttribute("email"));
        user.setPicture(principal.getAttribute("picture") != null ? principal.getAttribute("picture") : principal.getAttribute("avatar_url"));
        return user;
    }

    @GetMapping("/h2/user/list")
    public List<User> userList() {
        return service.getUserList();
    }

    @PostMapping("/h2/user/register")
    public ResponseEntity<?> userRegistration(User user) {
        if (service.setUser(user).getClass() == UserEntity.class)
            return new ResponseEntity<>(user, HttpStatus.OK);
        else
            return null;
    }

    @PostMapping("/h2/user/register/json")
    public ResponseEntity<?> userRegistrationJSON(@RequestBody HashMap<String, Object> userHashMap) {
        log.warn(userHashMap.get("nickname"));
        log.warn(userHashMap.get("email"));
        log.warn(userHashMap.get("picture"));
        User user = new User();
        user.setNickname(userHashMap.get("nickname") != null ? userHashMap.get("nickname").toString() : null);
        user.setEmail(userHashMap.get("email") != null ? userHashMap.get("email").toString() : null);
        user.setPicture(userHashMap.get("picture") != null ? userHashMap.get("picture").toString() : null);
        return new ResponseEntity<>(service.setUser(user), HttpStatus.OK);
    }
}
