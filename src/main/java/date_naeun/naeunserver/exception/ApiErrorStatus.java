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
     * Ingredient Api 관련 에러 코드
     */
    /* searchIngredient (검색) */
    INGR_NOT_RESULT(HttpStatusCode.BAD_REQUEST, "검색 결과가 없습니다."),

    /* UserIngredient 추가 및 삭제 */
    INGR_LIST_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "성분 list가 없습니다."),
    INGR_NOT_INTEGER(HttpStatusCode.BAD_REQUEST, "가 정수가 아닙니다."),
    INGR_DUPLICATED_ID(HttpStatusCode.BAD_REQUEST, "가 중복되었습니다."),
    INGR_ID_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "가 존재하지 않습니다."),
    INGR_PREFER_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "선호 성분이 등록되지 않았습니다."),
    INGR_DISLIKE_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "기피 성분이 등록되지 않았습니다."),

    /**
     * searchCosmetic Api 관련 에러 코드
     */
    CSMT_NOT_RESULT(HttpStatusCode.BAD_REQUEST, "검색 결과가 없습니다."),

    /**
     * 화장품 리뷰 Api 관련 에러 코드
     */
    REVIEW_NOT_RATING(HttpStatusCode.BAD_REQUEST, "별점이 없습니다."),
    REVIEW_NOT_CONTENT(HttpStatusCode.BAD_REQUEST, "리뷰 내용이 없습니다."),
    REVIEW_NOT_TEXTURE(HttpStatusCode.BAD_REQUEST, "발림성이 없습니다."),
    REVIEW_NOT_REPURCHASE(HttpStatusCode.BAD_REQUEST, "재구매의사가 없습니다."),
    REVIEW_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "등록된 리뷰가 없습니다."),

    /**
     * skinType Api 관련 에러 코드
     */
    SKINTYPE_NOT_EXIST(HttpStatusCode.BAD_REQUEST, "스킨타입이 없습니다."),
    SKINTYPE_NOT_VALID(HttpStatusCode.BAD_REQUEST, "유효하지 않은 스킨타입입니다.");


    private final HttpStatusCode code;
    private final String msg;

    ApiErrorStatus(HttpStatusCode code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
