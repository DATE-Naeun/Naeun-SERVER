package date_naeun.naeunserver.config.jwt;

import lombok.*;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String type;    // Signup / Login
    private String accessToken;
    private String refreshToken;

}
