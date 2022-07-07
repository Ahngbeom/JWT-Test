package com.ahng.myspringoauth2maven.JpaRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ahng.myspringoauth2maven.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
