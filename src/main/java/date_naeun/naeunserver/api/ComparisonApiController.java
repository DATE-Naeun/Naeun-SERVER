package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.dto.CosmeticComparisonDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.repository.ComparisonQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class ComparisonApiController {

    private final ComparisonQueryRepository comparisonQueryRepository;

    @PostMapping("/api/comparison")
    public ResultDto<List<CosmeticComparisonDto>> createCosmeticComparison(@RequestBody CosmeticComparisonRequestDto cosmeticComparisonRequestDto) {
        List<Cosmetic> cosmetics = comparisonQueryRepository.rankCosmetics(cosmeticComparisonRequestDto.cosmetics);
        List<CosmeticComparisonDto> result = cosmetics.stream()
                .map(CosmeticComparisonDto::new)
                .collect(toList());
        return ResultDto.of(HttpStatusCode.OK, "화장품 비교 결과 가져오기 성공", result);
    }

    @Data
    static class CosmeticComparisonRequestDto {
        Long[] cosmetics;
    }

    @GetMapping("/api/comparison/recommend/{category}")
    public ResultDto<List<CosmeticComparisonDto>> recommendCosmeticBySkinTypeAndCategory() {
        Long[] idList = {};
        List<Cosmetic> cosmetics = comparisonQueryRepository.rankCosmetics(idList);
        List<CosmeticComparisonDto> result = cosmetics.stream()
                .map(CosmeticComparisonDto::new)
                .collect(toList());

        return ResultDto.of(HttpStatusCode.OK, "피부타입&카테고리별 알고리즘 추천 화장품 리스트 가져오기 성공", result);
    }
}
