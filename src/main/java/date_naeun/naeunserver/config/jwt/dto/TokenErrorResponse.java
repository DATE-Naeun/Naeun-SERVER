package date_naeun.naeunserver.config.jwt.dto;

import date_naeun.naeunserver.config.jwt.exception.TokenStatus;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class TokenErrorResponse {

    private HttpStatus httpStatus;
    private String errorMessage;

    public TokenErrorResponse(TokenStatus tokenStatus) {

        switch (tokenStatus) {
            case INVALID_TOKEN:
                httpStatus = HttpStatus.BAD_REQUEST;
                errorMessage = "유효하지 않은 토큰입니다.";
                break;
            case EMPTY_TOKEN:
                httpStatus = HttpStatus.BAD_REQUEST;
                errorMessage = "토큰이 없습니다.";
                break;
            case EXPIRED_TOKEN:
                httpStatus = HttpStatus.UNAUTHORIZED;
                errorMessage = "토큰이 만료되었습니다.";
                break;
            case REFRESH_EXPIRED:
                httpStatus = HttpStatus.UNAUTHORIZED;
                errorMessage = "refresh 토큰이 만료되었습니다.";
        }
    }
}
