package org.example.core.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.User;
import org.example.core.domain.entity.repository.UserRepository;
import org.example.core.dto.UserCreateRequest;
import org.example.core.exception.CalendarException;
import org.example.core.exception.ErrorCode;
import org.example.core.util.Encryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Encryptor encryptor;

    @Transactional
    public User create(UserCreateRequest userCreateRequest) {
        userRepository.findByEmail(userCreateRequest.getEmail())
                .ifPresent(user -> {
                    throw new CalendarException(ErrorCode.ALREADY_EXISTS_USER);
                });

        User user = User.builder()
                .name(userCreateRequest.getName())
                .email(userCreateRequest.getEmail())
                .password(encryptor.encrypt(userCreateRequest.getPassword()))
                .birthday(userCreateRequest.getBirthday())
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public Optional<User> findPwMatchUser(String email, String password) {
//        return userRepository.findByEmail(email)
//                .map(user -> encryptor.isMatch(user.getPassword(), password) ? user : null);
        return userRepository.findByEmail(email)
                .map(user -> user.isMatched(encryptor, password) ? user : null);
    }

    @Transactional
    public User findByUserId(Long userId) {  // getByUserIdOrThrow
        return userRepository.findById(userId)
                .orElseThrow(() -> new CalendarException(ErrorCode.ALREADY_EXISTS_USER));
    }
}
