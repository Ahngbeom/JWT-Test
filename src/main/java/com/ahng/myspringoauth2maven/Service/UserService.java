package com.ahng.myspringoauth2maven.Service;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.Domain.UserEntity;

import java.util.List;

public interface UserService {
    List<User> getUserList();

    UserEntity setUser(User user);
}
