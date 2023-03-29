package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.core.domain.entity.User;
import org.example.core.dto.UserCreateRequest;
import org.example.core.exception.CalendarException;
import org.example.core.exception.ErrorCode;
import org.example.core.service.UserService;
import org.example.dto.LoginRequest;
import org.example.dto.SignUpRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    public final static String LOGIN_SESSION_KEY = "USER_ID";
    private final UserService userService;

    @Transactional
    public void signUp(SignUpRequest signUpRequest, HttpSession session) {
        /**
         * UserService 에 Create 를 담당한다. (이미 존재하는 경우의 유저 검증은 userService 의 몫)
         * 생성이 되면 session 에 담고 확인
         */
        final User user = userService.create(new UserCreateRequest(
                signUpRequest.getName(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getBirthday()
        ));
        session.setAttribute(LOGIN_SESSION_KEY, user.getId());

    }

    @Transactional
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
            throw new CalendarException(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Transactional
    public void logout(HttpSession session) {
        /**
         * 세션 제거 하고 끝
         */
        session.removeAttribute(LOGIN_SESSION_KEY);
    }
}
