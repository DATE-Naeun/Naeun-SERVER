package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.dto.CosmeticDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.CosmeticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CosmeticApiController {

    private final CosmeticService cosmeticService;

    @GetMapping("/api/cosmetic/search")
    public ResultDto<List<CosmeticDto>> searchCosmetic(@RequestParam String keyword) {
        List<Cosmetic> findCosmetic = cosmeticService.findByKeyword(keyword);
        List<CosmeticDto> collect = findCosmetic.stream()
                .map(CosmeticDto::new)
                .collect(Collectors.toList());

        return ResultDto.of(HttpStatus.OK, "화장품 검색 결과 가져오기 성공", collect);
    }
}
