package date_naeun.naeunserver.exception;

import lombok.Getter;

@Getter
public class UserCosmeticErrorException extends RuntimeException{

    private final HttpStatusCode code;
    private final String errorMsg;

    public UserCosmeticErrorException(ApiErrorStatus errorStatus, Object cosmeticId) {
        this.code = errorStatus.getCode();
        if (cosmeticId.equals(0L)) {
            this.errorMsg = errorStatus.name();
        } else {
            this.errorMsg = cosmeticId + errorStatus.name();
        }
    }
}
