package date_naeun.naeunserver.config.jwt.dto;

import date_naeun.naeunserver.config.exception.AuthErrorStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthErrorResponse {
    private HttpStatus httpStatus;
    private String errorMessage;

    public AuthErrorResponse(AuthErrorStatus authStatus) {
        switch (authStatus) {
            case GETUSER_FAILED:
                httpStatus = HttpStatus.UNAUTHORIZED;
                errorMessage = "회원 정보 조회 실패";
                break;
            case SOCIAL_TOKEN_EXPIRED:
                httpStatus = HttpStatus.UNAUTHORIZED;
                errorMessage = "만료된 소셜 토큰입니다.";
                break;
        }
    }
}
