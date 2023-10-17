package date_naeun.naeunserver.exception;

import lombok.Getter;

/**
 * API Error Code
 */
@Getter
public enum ApiErrorStatus {

    /**
     * 나의 화장대 Api 관련 에러 코드
     * (UserCosmetic Api)
     */
    LIST_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "화장품 list가 없습니다."),
    NOT_INTEGER(HttpStatusCode.BAD_REQUEST, "가 정수가 아닙니다."),
    DUPLICATED_ID(HttpStatusCode.BAD_REQUEST, "가 중복되었습니다."),
    NOT_EXIST(HttpStatusCode.BAD_REQUEST, "가 존재하지 않습니다.");

    private final HttpStatusCode code;
    private final String msg;

    ApiErrorStatus(HttpStatusCode code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
