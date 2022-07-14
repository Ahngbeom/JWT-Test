package com.ahng.myspringoauth2maven.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Kakao {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private Long expires_in;

    private String scope;

    private Long refresh_token_expires_in;

    public Kakao(String access_token, String token_type, String refresh_token, Long expires_in, String scope, Long refresh_token_expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
        this.scope = scope;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public void setRefresh_token_expires_in(Long refresh_token_expires_in) {
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
