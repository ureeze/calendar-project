package org.example.core.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserCreateRequest {
    private final String name;
    private final String email;
    private final String password;
    private final LocalDate birthday;

    @Builder
    public UserCreateRequest(String name, String email, String password, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
    }
}
