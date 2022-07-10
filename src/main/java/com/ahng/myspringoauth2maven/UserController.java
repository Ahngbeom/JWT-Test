package com.ahng.myspringoauth2maven;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.Domain.UserEntity;
import com.ahng.myspringoauth2maven.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LogManager.getLogger();
    private final UserService service;

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
        UserEntity userEntity = UserEntity.builder()
                .nickname((String) userHashMap.get("nickname"))
                .email((String) userHashMap.get("email"))
                .picture((String) userHashMap.get("picture"))
                .build();
        User user = new User(userHashMap.get("nickname").toString(), userHashMap.get("email").toString(), userHashMap.get("picture").toString());
        service.setUser(user);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }
}
