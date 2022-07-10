package com.ahng.myspringoauth2maven.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ahng.myspringoauth2maven.Domain.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
