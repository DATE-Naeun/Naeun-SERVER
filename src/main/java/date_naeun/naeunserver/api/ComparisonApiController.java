package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.CosmeticComparisonDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.repository.ComparisonQueryRepository;
import date_naeun.naeunserver.service.HistoryService;
import date_naeun.naeunserver.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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
        User user = getUser(userDetail);
        List<Cosmetic> cosmetics = comparisonQueryRepository.rankCosmetics(cosmeticComparisonRequestDto.cosmetics, user);
        List<CosmeticComparisonDto> result = cosmetics.stream()
                .map(CosmeticComparisonDto::new)
                .collect(toList());
      
        historyService.saveHistory(user, cosmetics);
        return ResultDto.of(HttpStatusCode.OK, "화장품 비교 결과 가져오기 성공", result);
    }

    @Data
    static class CosmeticComparisonRequestDto {
        Long[] cosmetics;
    }

    @GetMapping("/api/comparison/recommend/{category}")
    public ResultDto<List<CosmeticComparisonDto>> recommendCosmeticBySkinTypeAndCategory(
            @PathVariable int category,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        User user = getUser(userDetail);
        Long[] idList = comparisonQueryRepository.getCosmeticsByCategory(category);
        List<Cosmetic> cosmetics = comparisonQueryRepository.rankCosmetics(idList, user);
        List<CosmeticComparisonDto> result = cosmetics.stream()
                .map(CosmeticComparisonDto::new)
                .collect(toList());

        return ResultDto.of(HttpStatusCode.OK, "피부타입&카테고리별 알고리즘 추천 화장품 리스트 가져오기 성공", result);
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
