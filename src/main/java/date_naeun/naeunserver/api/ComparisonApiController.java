package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.CosmeticComparisonDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.exception.ApiErrorException;
import date_naeun.naeunserver.exception.ApiErrorStatus;
import date_naeun.naeunserver.exception.ApiErrorWithItemException;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.repository.ComparisonQueryRepository;
import date_naeun.naeunserver.service.HistoryService;
import date_naeun.naeunserver.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class ComparisonApiController {

    private final ComparisonQueryRepository comparisonQueryRepository;
    private final UserService userService;
    private final HistoryService historyService;

    @PostMapping("/api/comparison")
    public ResultDto<List<CosmeticComparisonDto>> createCosmeticComparison(
            @RequestBody CosmeticComparisonRequestDto cosmeticComparisonRequestDto,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        try {
            User user = userService.findUserById(userDetail.getId());
            if (cosmeticComparisonRequestDto.cosmetics == null || cosmeticComparisonRequestDto.cosmetics.length == 0) {
                throw new ApiErrorException(ApiErrorStatus.LIST_NOT_EXIST);
            }
            List<Cosmetic> cosmetics = comparisonQueryRepository.rankCosmetics(cosmeticComparisonRequestDto.cosmetics, user);
            List<CosmeticComparisonDto> result = cosmetics.stream()
                    .map(CosmeticComparisonDto::new)
                    .collect(toList());
            historyService.saveHistory(user, cosmetics);
            return ResultDto.of(HttpStatusCode.OK, "화장품 비교 결과 가져오기 성공", result);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorWithItemException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @Data
    static class CosmeticComparisonRequestDto {
        Long[] cosmetics;
    }

    @GetMapping("/api/comparison/recommend/{category}")
    public ResultDto<List<CosmeticComparisonDto>> recommendCosmeticBySkinTypeAndCategory(
            @PathVariable int category,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        try {
            User user = userService.findUserById(userDetail.getId());
            Long[] idList = comparisonQueryRepository.getCosmeticsByCategory(category);
            List<Cosmetic> cosmetics = comparisonQueryRepository.rankCosmetics(idList, user);
            List<CosmeticComparisonDto> result = cosmetics.stream()
                    .map(CosmeticComparisonDto::new)
                    .collect(toList());

            return ResultDto.of(HttpStatusCode.OK, "피부타입&카테고리별 알고리즘 추천 화장품 리스트 가져오기 성공", result);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
