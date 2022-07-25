package com.ahng.myspringoauth2maven.Controller;

import com.ahng.myspringoauth2maven.DTO.UserDTO;
import com.ahng.myspringoauth2maven.Entity.Authority;
import com.ahng.myspringoauth2maven.Entity.User;
import com.ahng.myspringoauth2maven.JWT.JWTFilter;
import com.ahng.myspringoauth2maven.Service.UserService;
import com.ahng.myspringoauth2maven.Utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.signUp(userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(e.getMessage());
        }

    }

    // 유저 정보 조회 API
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')") // 접근 허용된 권한 리스트 (로그인되어져있는 모든 유저가 접근 가능)
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().orElse(null));
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')") // 접근 허용된 권한 리스트 (관리자만 접근 가능)
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).orElse(null));
    }

    @GetMapping("/user/list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<User>> getUserList() {
        return new ResponseEntity<>(userService.getUserListWithAuthorities().orElse(null), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader(JWTFilter.AUTHORIZATION_HEADER, null);

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals("refresh_token")) {
                        log.info("Remove Refresh Token for Logout - " + c.getValue());
                        c.setMaxAge(0);
                        c.setValue(null);
                        c.setPath("/"); // 쿠키를 생성했을 때 설정한 Path 값도 동일하게 지정해주어야 동일한 쿠키를 대체 또는 삭제할 수 있다.
                        response.addCookie(c);
                    }
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/user/username-availability")
    public ResponseEntity<Boolean> usernameAvailability(String username) {
        if (username.toUpperCase().contains("ADMIN") || username.toUpperCase().contains("ADMINISTRATOR"))
            return ResponseEntity.ok(false);
        return ResponseEntity.ok(!userService.getUserWithAuthorities(username).isPresent());
    }
}
