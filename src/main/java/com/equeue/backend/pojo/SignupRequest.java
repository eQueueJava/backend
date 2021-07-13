package com.equeue.backend.pojo;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignupRequest {

    @Size(min = 4, max = 30)
    private String username;
    @Email
    @Size(min = 4, max = 30)
    private String email;
    private Set<String> roles;
    private String password;
}
