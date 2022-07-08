package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.Entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> getUserList();

    boolean setUser(UserEntity user);
}
