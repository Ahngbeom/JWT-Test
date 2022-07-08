package com.ahng.myspringoauth2maven;

import java.util.HashMap;
import java.util.List;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.Entity.UserEntity;
import com.ahng.myspringoauth2maven.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class MySpringOauth2MavenApplication {

	private static final Logger log = LogManager.getLogger();
	private final UserService service;
	
	// @GetMapping("/user")
    // public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
    //     return Collections.singletonMap("name", principal.getAttribute("name"));
    // }

	@GetMapping("/h2/user/list")
	public List<UserEntity> userList() {
		return service.getUserList();
	}

	@PostMapping("/h2/user/register")
	public ResponseEntity<?> userRegistration(UserEntity user) {
//		user = UserEntity.builder()
//						.nickname(user.getNickname())
//								.email(user.getEmail())
//										.picture(user.getPicture())
//				.build();
		log.warn(user.getNickname());
		log.warn(user.getEmail());
		log.warn(user.getPicture());
		service.setUser(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/h2/user/register/json")
	public ResponseEntity<?> userRegistrationJSON(@RequestBody HashMap<String, Object> user) {
		log.warn(user.get("nickname"));
		log.warn(user.get("email"));
		log.warn(user.get("picture"));
		UserEntity userEntity = UserEntity.builder()
				.nickname((String) user.get("nickname"))
				.email((String) user.get("email"))
				.picture((String) user.get("picture"))
				.build();
		service.setUser(userEntity);
		return new ResponseEntity<>(userEntity, HttpStatus.OK);
	}

	public static void main(String[] args) {
		SpringApplication.run(MySpringOauth2MavenApplication.class, args);
	}

}
