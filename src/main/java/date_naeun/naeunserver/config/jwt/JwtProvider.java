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

    private CustomUserDetailService userDetailService;

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

    /**
     * UsernamePasswordAuthenticationToken으로 보내 인증된 유저인지 확인
     */
    public Authentication getAuthentication(String accessToken) throws ExpiredJwtException {
        Claims claims = getTokenBody(accessToken);
        // email로 UserDetail 가져오기
        CustomUserDetail userDetail = userDetailService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetail, "", userDetail.getAuthorities());
    }

    /**
     *토큰 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims getTokenBody(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();
    }

}
