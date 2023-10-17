package date_naeun.naeunserver.exception;

public enum ApiErrorStatus {
    /**
     * 나의 화장대 Api 관련 에러 코드
     * (UserCosmetic Api)
     */
    LIST_NOT_EXIST("화장품 list가 없습니다."),
    NOT_INTEGER("가 정수가 아닙니다."),
    DUPLICATED_ID("가 중복되었습니다."),
    NOT_EXIST("가 존재하지 않습니다.");

    ApiErrorStatus(String s) {
    }
}
