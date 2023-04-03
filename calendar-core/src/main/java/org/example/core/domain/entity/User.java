package org.example.core.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.core.util.Encryptor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {


    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private LocalDate birthday;
    @Column
    private LocalDateTime createdAt = LocalDateTime.now();


    @Builder
    public User(String name, String email, String password, LocalDate birthday) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public boolean isMatched(Encryptor encryptor, String password) {
        return encryptor.isMatch(password, this.password);
    }
}
