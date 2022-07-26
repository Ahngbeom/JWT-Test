package com.ahng.myspringoauth2maven.DTO;

import com.ahng.myspringoauth2maven.Entity.Authority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuth2UserDTO {

    @NotNull
    @Size(min = 3, max = 100)
    private String email;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(min = 3, max = 200)
    private String picture;

}
