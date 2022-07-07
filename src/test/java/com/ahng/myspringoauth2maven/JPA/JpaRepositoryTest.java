package com.ahng.myspringoauth2maven.JPA;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.ahng.myspringoauth2maven.Entity.User;
import com.ahng.myspringoauth2maven.JpaRepository.UserRepository;

@SpringBootTest // SpringBootTest에서 ExtendWith(SpringExtension.class) 메타 어노테이션을 이미 사용하고 있음
@Rollback(false)
public class JpaRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private static final Logger log = LogManager.getLogger();

	@Test
	public void name() {
		User user = new User("bahn", "bahn@student.42seoul.kr", null);

		userRepository.save(user);

		log.info("ID: " + user.getId());
		log.info("Nickname: " + user.getNickname());

		User retrivedUser = userRepository.findById(user.getId()).orElse(null);

		assert retrivedUser != null;
		assertEquals(retrivedUser.getNickname(), "bahn");
		assertEquals(retrivedUser.getEmail(), "bahn@student.42seoul.kr");
		assertNull(retrivedUser.getPicture());
	}

	
}
