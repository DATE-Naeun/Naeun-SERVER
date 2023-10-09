package date_naeun.naeunserver.service;

import date_naeun.naeunserver.config.jwt.JwtProvider;
import date_naeun.naeunserver.config.exception.TokenErrorException;
import date_naeun.naeunserver.config.exception.TokenStatus;
import date_naeun.naeunserver.domain.RefreshToken;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.repository.RefreshTokenRepository;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * 리프레시 토큰 발급
     */
    public String createRefreshToken(String email) {
        User user = userRepository.findByEmail(email);
        //토큰 생성시간
        String newRefreshToken = jwtProvider.generateRefreshToken(email);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(newRefreshToken)
                .expiryDate(jwtProvider.getExpireMin(newRefreshToken))
                .user(user)
                .build();

        // db 저장
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    /**
     * 리프레시 토큰으로 액세스 토큰 재발급
     */
    public String reAccessToken(String token) {
        RefreshToken refreshToken = findRefreshToken(token);
        verifyExpiration(refreshToken);

        User user = refreshToken.getUser();

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole());

        return jwtProvider.generateAccessToken(claims, user.getEmail());
    }

    /**
     * 리프레시 토큰 조회
     */
    private RefreshToken findRefreshToken(String token) {
        try {
            return refreshTokenRepository.findByToken(token);
        } catch (NullPointerException e) {
            throw new TokenErrorException(TokenStatus.INVALID_TOKEN);
        }
    }

    /**
     * 만료시간 검증
     */
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(new Date()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenErrorException(TokenStatus.REFRESH_EXPIRED);
        }
    }
}
