package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.exception.AuthErrorException;
import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.UserDto;
import date_naeun.naeunserver.dto.UpdateUserNicknameRequestDto;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    @GetMapping("/api/user")
    public ResultDto<UserDto> getUserInfo(@AuthenticationPrincipal CustomUserDetail userDetail) {
        try {
            User user = userService.findUserById(userDetail.getId());
            return ResultDto.of(HttpStatusCode.OK, "유저 조회 성공", new UserDto(user));

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @PatchMapping("/api/user")
    public ResultDto<Object> updateUserNickname(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        userService.update(userDetail.getId(), updateUserNicknameRequestDto);
        return ResultDto.of(HttpStatusCode.OK, "사용자 닉네임 설정 성공", null);
    }

    @DeleteMapping("/api/user")
    public ResultDto<Object> deleteUser(@AuthenticationPrincipal CustomUserDetail userDetail) {
        userService.delete(userDetail.getId());
        return ResultDto.of(HttpStatusCode.OK, "사용자 탈퇴 성공", null);
    }
}
