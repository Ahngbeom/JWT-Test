package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.DTO.UserDTO;
import com.ahng.myspringoauth2maven.Entity.Authority;
import com.ahng.myspringoauth2maven.Entity.User;
import com.ahng.myspringoauth2maven.Repository.UserRepository;
import com.ahng.myspringoauth2maven.Utils.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원 가입 로직
    @Transactional
    public User signUp(UserDTO userDTO) {
        // 데이터베이스 안에 인자로 넘어온 UserDTO 객체에 해당하는 유저가 기존에 등록되어있는지 검증
        if (userRepository.findOneWithAuthoritiesByUsername(userDTO.getUsername()).orElse(null) != null)
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");

        // 권한 정보 생성
//        Authority authority = Authority.builder()
//                .authorityName("ROLE_USER")
//                .build();

        // UserDTO 객체를 기준으로 UserEntity 객체 생성
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .nickname(userDTO.getNickname())
//                .authorities(Collections.singleton(authority))
                .authorities(userDTO.getAuthorities())
                .activated(true)
                .build();

        // DB에 저장
        return userRepository.save(user);
    }

    // 특정 유저에 대한 유저 정보 및 권한 정보를 조회 (관리자 전용?)
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    // 자신의 유저 정보와 권한 정보를 조회 (로그인되어있을 경우에만 수행 가능?)
    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

}
