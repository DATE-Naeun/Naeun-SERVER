package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.dto.CosmeticDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.CosmeticService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CosmeticApiController {

    private final CosmeticService cosmeticService;

    @PostMapping("/api/cosmetic")
    public ResultDto<CreateCosmeticFromPhotoResponseDto> createCosmeticFromPhoto(@RequestBody CreateCosmeticFromPhotoRequestDto createCosmeticFromPhotoRequestDto) {
        Long cosmeticId = cosmeticService.createCosmeticFromPhoto(createCosmeticFromPhotoRequestDto.cosmeticName, createCosmeticFromPhotoRequestDto.brand,
                createCosmeticFromPhotoRequestDto.category, createCosmeticFromPhotoRequestDto.ingredients).getId();
        return ResultDto.of(HttpStatus.CREATED, "촬영으로 비교할 화장품 추가하기 성공", new CreateCosmeticFromPhotoResponseDto(cosmeticId));
    }

    @Data
    static class CreateCosmeticFromPhotoRequestDto {
        String cosmeticName;
        String brand;
        String category;
        List<String> ingredients;
    }

    @Data
    @AllArgsConstructor
    static class CreateCosmeticFromPhotoResponseDto {
        Long cosmeticId;
    }

    @GetMapping("/api/cosmetic/search")
    public ResultDto<List<CosmeticDto>> searchCosmetic(@RequestParam String keyword) {
        List<Cosmetic> findCosmetic = cosmeticService.findByKeyword(keyword);
        List<CosmeticDto> collect = findCosmetic.stream()
                .map(CosmeticDto::new)
                .collect(Collectors.toList());

        return ResultDto.of(HttpStatus.OK, "화장품 검색 결과 가져오기 성공", collect);
    }
}
