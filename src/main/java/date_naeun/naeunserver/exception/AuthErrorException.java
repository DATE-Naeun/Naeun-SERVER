package date_naeun.naeunserver.exception;

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
