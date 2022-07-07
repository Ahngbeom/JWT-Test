package com.ahng.myspringoauth2maven.JPA;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.ahng.myspringoauth2maven.Entity.User;
import com.ahng.myspringoauth2maven.JpaRepository.UserRepository;

@SpringBootTest
@Rollback(false)
public class JpaRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void name() {
		User user = new User("bahn", "bahn@student.42seoul.kr", null);
		// user.setNinkname("bahn");
		// user.setEmail("bahn@student.42seoul.kr");

		System.out.println(user.getNinkname());

		User retrivedUser = userRepository.findById(user.getId()).get();

		assertEquals(retrivedUser.getNinkname(), "bahn");
		assertEquals(retrivedUser.getEmail(), "bahn@student.42seoul.kr");
		assertEquals(retrivedUser.getPicture(), null);
	}

	
}
