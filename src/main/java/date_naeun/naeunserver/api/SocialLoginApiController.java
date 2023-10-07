package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.dto.TokenDto;
import date_naeun.naeunserver.config.jwt.dto.TokenRefreshDto;
import date_naeun.naeunserver.config.jwt.exception.TokenStatus;
import date_naeun.naeunserver.config.jwt.exception.TokenErrorException;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.external.client.kakao.dto.KakaoUserInfo;
import date_naeun.naeunserver.external.client.kakao.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


/**
 * 회원가입/로그인 API
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialLoginApiController {

    private final KakaoUserService kakaoUserService;

    /**
     * 카카오 소셜 회원가입/로그인
     */
    @PostMapping("/api/auth/social")
    public ResultDto<Object> socialLogin(@RequestHeader HttpHeaders headers) {

        String accessToken = Objects.requireNonNull(headers.getFirst("Authorization")).substring(7);
        log.info("access Token = {}", accessToken);

        if (accessToken.equals("")) throw new TokenErrorException(TokenStatus.EMPTY_TOKEN);

        // token으로 카카오 사용자 정보 가져오기
        KakaoUserInfo kakaoUserInfo = kakaoUserService.getKakaoUserInfo(accessToken);

        // 회원가입/로그인 후 JWT 토큰 발급
        TokenDto tokenDto = kakaoUserService.joinorLogin(kakaoUserInfo);

        if (tokenDto.getType().equals("Signup")) {
            return ResultDto.of(HttpStatus.CREATED, "회원 가입 성공", tokenDto);
        } else {
            return ResultDto.of(HttpStatus.CREATED, "로그인 성공", tokenDto);
        }
    }

    /**
     * 액세스 토큰 재발급 요청
     */
    @GetMapping("/api/auth/getnewtoken")
    public ResultDto<Object> getNewToken(@RequestHeader HttpHeaders headers) {
        try {
            String refreshToken = Objects.requireNonNull(headers.getFirst("Authorization")).substring(7);
            String accessToken = kakaoUserService.regenerateToken(refreshToken);
            return ResultDto.of(HttpStatus.OK, "토큰 재발급", TokenRefreshDto.of(accessToken));
        } catch (NullPointerException e) {
            throw new TokenErrorException(TokenStatus.EMPTY_TOKEN);
        }
    }
}
