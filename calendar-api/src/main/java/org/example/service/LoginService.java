package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.User;
import org.example.core.dto.UserCreateRequest;
import org.example.core.service.UserService;
import org.example.dto.LoginRequest;
import org.example.dto.SignUpRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final static String LOGIN_SESSION_KEY = "USER_ID";
    private final UserService userService;

    public void signUp(SignUpRequest signUpRequest, HttpSession session) {

        UserCreateRequest userCreateRequest = UserCreateRequest.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .birthday(signUpRequest.getBirthday())
                .build();
        final User user = userService.create(userCreateRequest);
        session.setAttribute(LOGIN_SESSION_KEY, user.getId());

    }

    public void login(LoginRequest loginRequest, HttpSession session) {
        /**
         * 세션 같이 있으면 리턴
         * 세션 같이 없으면 비밀번호 체크 후에 로그인 & 세션에 담고 리턴
         */
        final Long userId = (Long) session.getAttribute(LOGIN_SESSION_KEY);
        if (userId != null) {
            return;
        }
        final Optional<User> user = userService.findPwMatchUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (user.isPresent()) {
            session.setAttribute(LOGIN_SESSION_KEY, user.get().getId());
        } else {
            throw new RuntimeException("password of email not match");
        }
    }

    public void logout(HttpSession session) {
        /**
         * 세션 제거 하고 끝
         */
        session.removeAttribute(LOGIN_SESSION_KEY);
    }
}
