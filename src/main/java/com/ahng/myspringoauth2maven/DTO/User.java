package com.ahng.myspringoauth2maven.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User {

    private Long id;
    private String nickname;
    private String email;
    private String picture;

    public User(String nickname, String email, String picture) {
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
