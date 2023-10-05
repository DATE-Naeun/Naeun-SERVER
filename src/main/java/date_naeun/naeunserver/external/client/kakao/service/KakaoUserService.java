package date_naeun.naeunserver.external.client.kakao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import date_naeun.naeunserver.config.jwt.JwtProvider;
import date_naeun.naeunserver.config.jwt.TokenDto;
import date_naeun.naeunserver.domain.Role;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.external.client.kakao.dto.KakaoUserInfo;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KakaoUserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * 카카오 액세스 토큰으로 카카오 사용자 정보 받아오는 메서드
     * @param kakaoToken : access token
     * @return 사용자 정보를 담은 Dto
     */
    public KakaoUserInfo getKakaoUserInfo(String kakaoToken) {

        // HttpHeader 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + kakaoToken);

        // HttpHeader 담기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        // 사용자 정보 요청 (POST)
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class);

        // Http 응답 (JSON)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 카카오 사용자 정보
        Map<String, Object> originAttributes = null;

        try {
            originAttributes = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            log.info("originAttributes = {}", originAttributes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        if (originAttributes == null) throw new NullPointerException("카카오 사용자 정보 받아오기 실패");

        return new KakaoUserInfo(originAttributes);
    }

    /**
     * 회원 가입/로그인 후 사용자 반환
     * - 이메일로 DB에 존재하는 회원인지 조회
     */
    public TokenDto joinorLogin(KakaoUserInfo kakaoUserInfo) {
        String email = kakaoUserInfo.getEmail();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            // 회원 가입 후 토큰 발급
            return createTokens(join(kakaoUserInfo), "Signup");
        }

        return createTokens(user, "Login");
    }

    /**
     * 회원 가입
     */
    @Transactional
    public User join(KakaoUserInfo kakaoUserInfo) {
        User newUser = User.builder()
                        .name(kakaoUserInfo.getName())
                        .email(kakaoUserInfo.getEmail())
                        .build();
        userRepository.save(newUser);
        log.info("join 성공 = {}", newUser.getName());
        return newUser;
    }

    /**
     * JWT토큰 발급
     *@paramuser: 현재 로그인한 user
     *@paramtype: signup / login
     */
    private TokenDto createTokens(User user, String type) {
        // Access Token 생성
        log.info("Token 생성 시작");
        String accessToken = delegateAccessToken(user.getEmail(), user.getRole());
        log.info("Access Token = {}", accessToken);

        // Refresh Token 생성
        String refreshToken = delegateRefreshToken(user.getEmail());
        log.info("Refresh Token = {}", refreshToken);

        return new TokenDto(type, accessToken, refreshToken);
    }

    /**
     *  Access Token 생성
     */
    private String delegateAccessToken(String email, Role role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email); // Principal
        claims.put("role", role);

        return jwtProvider.generateAccessToken(claims, email);
    }

    /**
     *  Refresh Token 생성
     */
    private String delegateRefreshToken(String email) {
        return jwtProvider.generateRefreshToken(email);
    }
}