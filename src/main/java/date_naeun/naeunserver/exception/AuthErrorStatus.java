package date_naeun.naeunserver.exception;

public enum AuthErrorStatus {
    GET_SOCIAL_INFO_FAILED( "소셜 로그인 회원 정보 조회 실패"),
    GET_USER_FAILED("회원 정보 조회 실패"),
    SOCIAL_TOKEN_EXPIRED("소셜 토큰이 만료되었습니다.");

    AuthErrorStatus(String s) {
    }
}
