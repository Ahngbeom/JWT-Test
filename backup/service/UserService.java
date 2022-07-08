package com.ahng.myspringoauth2maven.service;

import org.springframework.stereotype.Service;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id).get();
    }
}