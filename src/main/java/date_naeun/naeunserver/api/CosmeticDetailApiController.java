package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.CosmeticDetailDto;
import date_naeun.naeunserver.dto.IngredientDetailDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.CosmeticService;
import date_naeun.naeunserver.service.IngredientService;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
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

        // Request Header에서 accessToken 값을 추출
        User user = getUser(userDetail);

        // 화장품 가져오기
        Cosmetic cosmetic = cosmeticService.getCosmeticInfo(cosmeticId);

        // 성분리스트 가져오기
        List<Ingredient> ingreList = ingrService.findIngrList(cosmetic);

        if (ingreList == null) {
            return ResultDto.of(HttpStatus.OK, "성분 리스트가 비어 있습니다.", null);
        }

        // 성분 Detail 리스트
        List<IngredientDetailDto> ingr_collect = ingreList.stream()
                .map(IngredientDetailDto::new)
                .collect(Collectors.toList());

        CosmeticDetailDto collect = new CosmeticDetailDto(cosmetic, ingr_collect);

        // 나의 화장대에 있는 화장품인지 확인
        if (user.getUserCosmeticList().contains(cosmeticId)) {
            collect.setIsUserCosmetic(Boolean.TRUE);
        }

        return ResultDto.of(HttpStatus.OK, "화장품 상세 정보 가져오기 성공", collect);
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
