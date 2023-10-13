package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.SkinTypeDetailDto;
import date_naeun.naeunserver.dto.UpdateSkinTypeRequestDto;
import date_naeun.naeunserver.service.SkinTypeService;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SkinTypeApiController {

    private final SkinTypeService skinType_service;
    private final UserService user_service;

    @GetMapping("/api/skintype/detail")
    public ResultDto skinType(@RequestParam String skinType) {

        try {
            SkinType findSkinType = skinType_service.findSkinType(skinType.toUpperCase());
            List<SkinType> findAllSkinType = skinType_service.findAllSkinType();

            if (findAllSkinType.isEmpty()) {
                return ResultDto.of(HttpStatus.BAD_REQUEST, "스킨타입이 없습니다.", null);
            }

            System.out.println(findSkinType);

            if (findSkinType == null) {
                return ResultDto.of(HttpStatus.BAD_REQUEST, "유효하지 않은 스킨타입입니다.", null);
            }

            return ResultDto.of(HttpStatus.OK, "피부타입 상세 정보 가져오기 성공", new SkinTypeDetailDto(findSkinType));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }

    }


    @PatchMapping("/api/skintype")
    public ResultDto<Object> updateSkinType(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid UpdateSkinTypeRequestDto request) {
        try {
            skinType_service.update(userDetail.getId(), request);
            return ResultDto.of(HttpStatus.OK, "사용자 피부타입 설정 성공", null);
        } catch (Exception e) {
            e.printStackTrace();;
            return ResultDto.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
