package org.example.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
public class SignUpRequest {
    private final String name;
    private final String email;
    private final String password;
    private final LocalDate birthday;
}
