package org.example.exception;

import org.example.core.exception.ErrorCode;
import org.springframework.http.HttpStatus;

// 헬퍼클래스.
// 하위의 모든 메서드들이 static 으로만 선언되어 있는 메서드를 가지고 있는 클래스들은 보통 utils,helper 이런식의 이름 많이 사용한다.
// 그런경우에는 의미없는 기본생성자는 막아야 한다.
// 생성자 선언 후 private 접근지정자 추가. 혹은 우회해서 추상클래스화 하면 생성자를 문법적으로 불러올 수 없다.
public abstract class ErrorHttpStatusMapper {
    public static HttpStatus mapToStatus(ErrorCode errorCode) {
        switch (errorCode) {
            case ALREADY_EXISTS_USER:
            case VALIDATION_FAIL:
            case BAD_REQUEST:
            case EVENT_CREATE_OVERLAPPED_PERIOD:
                return HttpStatus.BAD_REQUEST;
            case PASSWORD_NOT_MATCH:
            case USER_NOT_FOUND:
                return HttpStatus.UNAUTHORIZED;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
