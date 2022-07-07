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
public class User {

	// 기본키 생성
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String email;

	@Column
	private String picture;

	@Builder
	public User(String nickname, String email, String picture) {
		this.nickname = nickname;
		this.email = email;
		this.picture = picture;
	}
	
	
	public Long getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public String getEmail() {
		return email;
	}
	
	public String getPicture() {
		return picture;
	}

	public void setNickname(String ninkname) {
		this.nickname = ninkname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
