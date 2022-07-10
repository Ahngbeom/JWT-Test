package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.Domain.UserEntity;
import com.ahng.myspringoauth2maven.DAO.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger();

    private final UserRepository repository;
    
    @Override
    public List<User> getUserList() {

        List<UserEntity> userEntityList = repository.findAll();
        List<User> userList = new ArrayList<>();
        userEntityList.forEach(userEntity -> userList.add(new User(userEntity.getId(), userEntity.getNickname(), userEntity.getEmail(), userEntity.getPicture())));
        return userList;
    }

    @Override
    public UserEntity setUser(User user) {
        return repository.save(UserEntity.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .picture(user.getPicture())
                .build());
    }

    @PostConstruct
    public void init() {
        UserEntity user = UserEntity.builder()
                .nickname("bahn")
                .email("bbu0704@gmail.com")
                .picture(null)
                .build();
        log.warn(user.getId());
        log.warn(user.getNickname());

        log.info(repository.save(user));
    }
}
