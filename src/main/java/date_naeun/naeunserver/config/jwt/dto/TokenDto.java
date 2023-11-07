package date_naeun.naeunserver.config.jwt.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String type;    // Signup / Login
    private String accessToken;
    private String refreshToken;

}
