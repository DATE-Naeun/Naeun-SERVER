package date_naeun.naeunserver.config.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;


@Slf4j
@Component
@NoArgsConstructor
public class JwtProvider {

    private static final Long accessTokenValidationTime = 30 * 60 * 1000L;   //30분

    private static final Long refreshTokenValidationTime = 7 * 24 * 60 * 60 * 1000L;  //7일

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    /**
     *  인증된 사용자에게 최초 발급할 Access Token 생성
     */
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject) {
        //토큰 생성시간
        Instant now = Instant.from(OffsetDateTime.now());

        return Jwts.builder()
                .setSubject(subject)    // Subject: JWT 에 대한 이름 추가(여기서는 이메일에 해당)
                .setClaims(claims)      // Claims: 사용자 관련 정보
                .setExpiration(Date.from(now.plusMillis(accessTokenValidationTime)))
                .signWith(key)
                .compact();
    }

    /**
     * Refresh Token 생성 메서드
     * - Access Token이 만료되었을 경우 생성
     */
    public String generateRefreshToken(String subject) {
        //토큰 생성시간
        Instant now = Instant.from(OffsetDateTime.now());

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(Date.from(now.plusMillis(refreshTokenValidationTime)))
                .signWith(key)
                .compact();
    }

}
