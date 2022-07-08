package com.ahng.myspringoauth2maven.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class UserEntity {

	// 기본키 생성
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "nickname")
	private String nickname;

	@Column(nullable = false, name = "email")
	private String email;

	@Column(name = "picture")
	private String picture;

	@Builder
	public UserEntity(String nickname, String email, String picture) {
		this.nickname = nickname;
		this.email = email;
		this.picture = picture;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
