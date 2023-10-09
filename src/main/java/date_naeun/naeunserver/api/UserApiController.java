package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.UserDto;
import date_naeun.naeunserver.dto.UpdateUserNicknameRequestDto;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;

    /** TO DO
     * exception 관련 코드 추가
     */

    @GetMapping("/api/user")
    public ResultDto<UserDto> getUserInfo(@AuthenticationPrincipal CustomUserDetail userDetail) {
        User user = getUser(userDetail);
        return ResultDto.of(HttpStatus.OK, "유저 조회 성공", new UserDto(user));
    }

    @PatchMapping("/api/user")
    public ResultDto<Object> updateUserNickname(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        userService.update(userDetail.getId(), updateUserNicknameRequestDto);
        return ResultDto.of(HttpStatus.OK, "사용자 닉네임 설정 성공", null);
    }

    @DeleteMapping("/api/user")
    public ResultDto<Object> deleteUser(@AuthenticationPrincipal CustomUserDetail userDetail) {
        userService.delete(userDetail.getId());
        return ResultDto.of(HttpStatus.OK, "사용자 탈퇴 성공", null);
    }

    /**
     * JWT 토큰으로 사용자를 받아오는 메서드
     */
    private User getUser(CustomUserDetail userDetail) {
        if (userDetail == null) {
            throw new EntityNotFoundException("해당 토큰으로 사용자를 조회할 수 없습니다.");
        }
        // accessToken 검증 후 생성된 userDetail의 id로 user 찾아서 생성
        return userService.findUserById(userDetail.getId());
    }
}
