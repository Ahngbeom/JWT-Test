package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.Entity.UserEntity;
import com.ahng.myspringoauth2maven.JpaRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger();

    private final UserRepository repository;
    
    @Override
    public List<UserEntity> getUserList() {
        return repository.findAll();
    }

    @Override
    public boolean setUser(UserEntity user) {
        return repository.save(user) != null;
    }

    @PostConstruct
    public void init() {
        UserEntity user = UserEntity.builder()
                .nickname("bahn")
                .email("bbu0704@gmail.com")
                .picture(null)
                .build();
        log.info(user);

        log.info(repository.save(user));
    }
}
