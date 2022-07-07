package com.ahng.myspringoauth2maven.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ahng.myspringoauth2maven.dto.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
