package date_naeun.naeunserver.config.exception;

import lombok.Getter;

public class AuthErrorException extends RuntimeException {

    @Getter
    private final AuthErrorStatus authStatus;

    public AuthErrorException(AuthErrorStatus authStatus) {

        this.authStatus = authStatus;
    }
}
