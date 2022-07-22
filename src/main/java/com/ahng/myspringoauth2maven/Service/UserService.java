package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.DTO.UserDTO;
import com.ahng.myspringoauth2maven.Entity.Authority;
import com.ahng.myspringoauth2maven.Entity.User;
import com.ahng.myspringoauth2maven.Repository.AuthorityRepository;
import com.ahng.myspringoauth2maven.Repository.UserRepository;
import com.ahng.myspringoauth2maven.Utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostConstruct
    public void setAdmin() throws RuntimeException {
        /*
            Spring Bean LifeCycle CallBack - @PostConstruct
            빈 생명주기 콜백: 스프링 빈이 생성된 후 의존관계 주입이 완료되거나 죽기 직전에 스프링 빈 안에 존재하는 메소드를 호출해주는 기능
            초기화 콜백 함수 setAdmin 함수를 추가하여 H2 데이터베이스에 Admin 계정을 등록한다.
         */
        try {
            Authority authority = Authority.builder()
                    .authorityName("ROLE_ADMIN")
                    .build();
            if (authorityRepository.save(authority).getClass() != Authority.class)
                throw new RuntimeException("ERROR SAVED ADMIN AUTHORITY ON AUTHORITY TABLE");

            authority = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();
            if (authorityRepository.save(authority).getClass() != Authority.class)
                throw new RuntimeException("ERROR SAVED USER AUTHORITY SAVE ON AUTHORITY TABLE");

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        log.info("SUCCESS SAVE ON AUTHORITY TABLE");
        try {
            Set<Authority> adminAuthorities = new HashSet<>();
            adminAuthorities.add(Authority.builder().authorityName("ROLE_ADMIN").build());
            adminAuthorities.add(Authority.builder().authorityName("ROLE_USER").build());

            User user = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .nickname("ADMIN")
                    .authorities(adminAuthorities)
                    .activated(true)
                    .build();
            if (userRepository.save(user).getClass() != User.class)
                throw new RuntimeException("ERROR SAVE ON USER TABLE");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        log.info("SUCCESS SAVE ON USERS TABLE");
    }

    @Transactional
    public User signUp(UserDTO userDTO) {
        // 회원 가입 로직

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


    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        // 특정 유저에 대한 유저 정보 및 권한 정보를 조회 (관리자 전용)
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        // 자신의 유저 정보와 권한 정보를 조회
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    @Transactional(readOnly = true)
    public Optional<List<User>> getUserListWithAuthorities() {
        return Optional.of(userRepository.findAll());
    }

}
