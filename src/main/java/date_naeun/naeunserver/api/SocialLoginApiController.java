package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.TokenDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.external.client.kakao.dto.KakaoUserInfo;
import date_naeun.naeunserver.external.client.kakao.service.KakaoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


/**
 * 카카오 소셜 회원가입/로그인 API
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialLoginApiController {

    private final KakaoUserService kakaoUserService;

    @PostMapping("/api/auth/social")
    public ResultDto<Object> socialLogin(@RequestHeader HttpHeaders headers) {

        String accessToken = headers.getFirst("accessToken");
        log.info("access Token = {}", accessToken);

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

}
