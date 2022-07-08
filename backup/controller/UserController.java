package com.ahng.myspringoauth2maven.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ahng.myspringoauth2maven.DTO.User;
import com.ahng.myspringoauth2maven.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserController {
	
	private final UserService userService;

	@GetMapping("/h2connect-example/user/{id}")
	public User getUser(@PathVariable Long id) {
		return userService.findById(id);
	}
}
