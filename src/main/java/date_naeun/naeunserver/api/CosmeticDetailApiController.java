package date_naeun.naeunserver.api;

import date_naeun.naeunserver.exception.AuthErrorException;
import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.CosmeticDetailDto;
import date_naeun.naeunserver.dto.IngredientDetailDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.service.CosmeticService;
import date_naeun.naeunserver.service.IngredientService;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CosmeticDetailApiController {

    private final UserService userService;
    private final CosmeticService cosmeticService;
    private final IngredientService ingrService;

    /**
     * 화장품 상세 페이지
     */
    @GetMapping("/api/cosmetic/detail/{cosmeticId}")
    public ResultDto<Object> getCosmeticDetail(@AuthenticationPrincipal CustomUserDetail userDetail, @PathVariable Long cosmeticId) {

        try {
            // 로그인한 회원 정보
            User user = userService.findUserById(userDetail.getId());

            // 화장품 가져오기
            Cosmetic cosmetic = cosmeticService.getCosmeticInfo(cosmeticId);

            // 성분리스트 가져오기
            List<Ingredient> ingreList = ingrService.findIngrList(cosmetic);
            CosmeticDetailDto collect;

            if (ingreList != null) {
                // 성분 Detail 리스트
                List<IngredientDetailDto> ingr_collect = ingreList.stream()
                        .map(IngredientDetailDto::new)
                        .collect(Collectors.toList());

                collect = new CosmeticDetailDto(cosmetic, ingr_collect);
            } else {    // 성분 리스트가 비어 있는 경우
                collect = new CosmeticDetailDto(cosmetic, new ArrayList<>());
            }

            // 나의 화장대에 있는 화장품인지 확인
            if (user.getUserCosmeticList().contains(cosmeticId)) {
                collect.setIsUserCosmetic(Boolean.TRUE);
            }

            return ResultDto.of(HttpStatusCode.OK, "화장품 상세 정보 가져오기 성공", collect);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
