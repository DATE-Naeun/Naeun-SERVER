package date_naeun.naeunserver.config.jwt.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenErrorException extends RuntimeException {

    @Getter
    private final TokenStatus tokenStatus;

    public TokenErrorException(TokenStatus tokenStatus) {

        this.tokenStatus = tokenStatus;
    }

}
