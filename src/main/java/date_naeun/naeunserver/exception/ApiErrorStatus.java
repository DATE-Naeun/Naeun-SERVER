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
    /* 나의 화장대에 화장품 추가 */
    ID_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "화장품 id가 없습니다."),

    /* 나의 화장대에서 화장품 삭제 */
    LIST_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "화장품 list가 없습니다."),
    NOT_INTEGER(HttpStatusCode.BAD_REQUEST, "가 정수가 아닙니다."),
    DUPLICATED_ID(HttpStatusCode.BAD_REQUEST, "가 중복되었습니다."),
    NOT_EXIST(HttpStatusCode.BAD_REQUEST, "가 존재하지 않습니다."),

    /**
     * User Api 관련 에러 코드
     */

    /* 닉네임 변경 */
    DUPLICATED_USER_NAME(HttpStatusCode.CONFLICT, "중복된 유저 닉네임입니다."),
    INVALID_USER_NANE(HttpStatusCode.BAD_REQUEST, "유저 닉네임이 입력되지 않았습니다."),

    /**
     * 촬영으로 화장품 추가 Api 관련 에러 코드
     */
    INVALID_COSMETIC_NAME(HttpStatusCode.BAD_REQUEST, "제품명이 입력되지 않았습니다."),
    INVALID_BRAND_NAME(HttpStatusCode.BAD_REQUEST, "브랜드명이 입력되지 않았습니다."),
    INGREDIENT_LIST_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "성분 list가 없습니다.");

    private final HttpStatusCode code;
    private final String msg;

    ApiErrorStatus(HttpStatusCode code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
