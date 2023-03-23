package org.example.dto;

import lombok.Data;

import java.lang.ref.PhantomReference;

@Data
public class LoginReq {
    private final String email;
    private final String password;
}
