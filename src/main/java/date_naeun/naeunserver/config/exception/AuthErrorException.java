package date_naeun.naeunserver.config.exception;

import date_naeun.naeunserver.exception.HttpStatusCode;
import lombok.Getter;

@Getter
public class AuthErrorException extends RuntimeException {

    private final HttpStatusCode code;
    private final String errorMsg;

    public AuthErrorException(AuthErrorStatus authStatus) {
        this.code = HttpStatusCode.UNAUTHORIZED;
        this.errorMsg = authStatus.name();
    }
}
