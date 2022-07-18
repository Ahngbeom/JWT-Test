package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.DTO.UserDTO;
import com.ahng.myspringoauth2maven.Entity.Authority;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

//@Component
//public class InitAdmin implements InitializingBean {
//
//    private final UserService userService;
//
//    public InitAdmin(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        Set<Authority> adminAuthorities = new HashSet<>();
//        adminAuthorities.add(Authority.builder().authorityName("ROLE_ADMIN").build());
//        adminAuthorities.add(Authority.builder().authorityName("ROLE_USER").build());
//        userService.signUp(
//                UserDTO.builder()
//                        .username("admin")
//                        .password("admin")
//                        .nickname("ADMIN")
//                        .build(),
//                adminAuthorities
//        );
//    }

//    @PostConstruct
//    public void setAdmin() {
//        Set<Authority> adminAuthorities = new HashSet<>();
//        adminAuthorities.add(Authority.builder().authorityName("ROLE_ADMIN").build());
//        adminAuthorities.add(Authority.builder().authorityName("ROLE_USER").build());
//        userService.signUp(
//                UserDTO.builder()
//                        .username("admin")
//                        .password("admin")
//                        .nickname("ADMIN")
//                        .build(),
//                adminAuthorities
//        );
//    }
//}
