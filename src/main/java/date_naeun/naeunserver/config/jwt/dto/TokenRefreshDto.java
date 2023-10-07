package date_naeun.naeunserver.config.jwt.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRefreshDto {
    private String accessToken;

    public static TokenRefreshDto of(String accessToken) {
        return new TokenRefreshDto(accessToken);
    }
}
