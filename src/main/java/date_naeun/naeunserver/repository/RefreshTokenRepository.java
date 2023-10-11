package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.config.exception.TokenErrorException;
import date_naeun.naeunserver.config.exception.TokenStatus;
import date_naeun.naeunserver.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /* refresh token을 redis에 저장 */
    public void save(RefreshToken refreshToken) {
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getUserId());
        redisTemplate.expire(refreshToken.getRefreshToken(), 21L, TimeUnit.DAYS);
    }

    public RefreshToken findById(final String refreshToken) {
        try {
            ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
            Long userId = valueOperations.get(refreshToken);

            if (Objects.isNull(userId)) {
                throw new NullPointerException("해당 refresh token으로 사용자를 조회할 수 없습니다.");
            }
            return new RefreshToken(refreshToken, userId);
        } catch (NullPointerException e) {
            throw new TokenErrorException(TokenStatus.REFRESH_EXPIRED);
        }
    }
}
