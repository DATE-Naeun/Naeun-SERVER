package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.UserDto;
import date_naeun.naeunserver.dto.UpdateUserNicknameRequestDto;
import date_naeun.naeunserver.repository.SkinTypeRepository;
import date_naeun.naeunserver.repository.UserRepository;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserApiController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final SkinTypeRepository skinTypeRepository;

    /** TO DO
     * exception 관련 코드 추가
     * Authorization 완료되면 url, parameter 교체
     */

    @GetMapping("/api/user/{id}")
    public ResultDto<UserDto> getUserInfo(@PathVariable Long id) {
        User user = userRepository.findUserWithSkinType(id);
        skinTypeRepository.find(user.getSkinType().getId());
        return ResultDto.of(HttpStatus.OK, "유저 조회 성공", new UserDto(user));
    }

    @PatchMapping("/api/user/{id}")
    public ResultDto<Object> updateUserNickname(@PathVariable Long id, @RequestBody UpdateUserNicknameRequestDto updateUserNicknameRequestDto) {
        userService.update(id, updateUserNicknameRequestDto);
        return ResultDto.of(HttpStatus.OK, "사용자 닉네임 설정 성공", null);
    }

    @DeleteMapping("/api/user/{id}")
    public ResultDto<Object> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResultDto.of(HttpStatus.OK, "사용자 탈퇴 성공", null);
    }
}
