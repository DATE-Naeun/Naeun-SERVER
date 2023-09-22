package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.User;
import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String userName;
    private SkinTypeDetailDto skinType;

    public UserDto(User user) {
        userId = user.getId();
        userName = user.getName();
        this.skinType = new SkinTypeDetailDto(user.getSkinType());
    }
}
