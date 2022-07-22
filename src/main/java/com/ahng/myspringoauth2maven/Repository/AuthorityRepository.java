package com.ahng.myspringoauth2maven.Repository;

import com.ahng.myspringoauth2maven.Entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
