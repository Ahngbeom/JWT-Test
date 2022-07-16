package com.ahng.myspringoauth2maven.Repository;

import com.ahng.myspringoauth2maven.Entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository 객체를 상속받아 findAll, save 등의 메소드를 기본적으로 사용할 수 있도록 구현한다. (DB CRUD 메소드)
public interface UserRepository extends JpaRepository<User, Long> {

    // username을 기준으로 User 정보를 가져올 때 권한 정보도 함께 가져오는 메소드
    @EntityGraph(attributePaths = "authorities") // 쿼리 수행 시 Lazy 조회가 아닌 Eager 조회로 authrities 정보를 가져오게한다. (즉시 로딩으로 연관된 데이터도 같이 조회한다.)
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
