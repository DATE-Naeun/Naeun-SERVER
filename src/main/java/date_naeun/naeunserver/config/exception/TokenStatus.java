package date_naeun.naeunserver.config.exception;

import org.springframework.http.HttpStatus;

public enum TokenStatus {
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EMPTY_TOKEN("토큰이 없습니다."),
    EXPIRED_TOKEN("토큰이 만료되었습니다."),
    REFRESH_EXPIRED("refresh 토큰이 만료되었습니다.");


    TokenStatus(String s) {
    }
}
