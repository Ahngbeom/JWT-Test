package com.ahng.myspringoauth2maven.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahng.myspringoauth2maven.DTO.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
