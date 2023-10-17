package date_naeun.naeunserver.exception;

import lombok.Getter;

@Getter
public class TokenErrorException extends RuntimeException {

    private HttpStatusCode code;
    private final String errorMsg;

    public TokenErrorException(TokenStatus tokenStatus) {
        switch (tokenStatus) {
            case INVALID_TOKEN:
            case EMPTY_TOKEN:
                this.code = HttpStatusCode.BAD_REQUEST;
                break;
            case EXPIRED_TOKEN:
            case REFRESH_EXPIRED:
                this.code = HttpStatusCode.UNAUTHORIZED;
                break;
        }
        this.errorMsg = tokenStatus.name();
    }

}
