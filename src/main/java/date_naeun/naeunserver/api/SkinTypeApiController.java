package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.SkinTypeDetailDto;
import date_naeun.naeunserver.dto.UpdateSkinTypeRequestDto;
import date_naeun.naeunserver.exception.ApiErrorException;
import date_naeun.naeunserver.exception.ApiErrorStatus;
import date_naeun.naeunserver.exception.AuthErrorException;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.service.SkinTypeService;
import date_naeun.naeunserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SkinTypeApiController {

    private final SkinTypeService skinTypeService;
    private final UserService userService;


    @GetMapping("/api/skintype/detail")
    public ResultDto skinType(@RequestParam String skinType) {
        try {
            SkinType findSkinType = skinTypeService.findSkinType(skinType.toUpperCase());
            List<SkinType> findAllSkinType = skinTypeService.findAllSkinType();

            if (findAllSkinType.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.SKINTYPE_NOT_EXIST);
            }

            if (findSkinType == null) {
                throw new ApiErrorException(ApiErrorStatus.SKINTYPE_NOT_VALID);
            }

            return ResultDto.of(HttpStatusCode.OK, "피부타입 상세 정보 가져오기 성공", new SkinTypeDetailDto(findSkinType));
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @PatchMapping("/api/skintype")
    public ResultDto<Object> updateSkinType(@AuthenticationPrincipal CustomUserDetail userDetail,
                                            @RequestBody @Valid UpdateSkinTypeRequestDto requestDto) {
        try {
            User user = userService.findUserById(userDetail.getId());
            skinTypeService.update(user, requestDto);
            return ResultDto.of(HttpStatusCode.CREATED, "사용자 피부타입 설정 성공", null);
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
