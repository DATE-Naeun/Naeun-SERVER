package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.dto.CosmeticDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.exception.ApiErrorException;
import date_naeun.naeunserver.exception.ApiErrorStatus;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.service.CosmeticService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
        return ResultDto.of(HttpStatusCode.CREATED, "촬영으로 비교할 화장품 추가하기 성공", new CreateCosmeticFromPhotoResponseDto(cosmeticId));
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
        try {
            List<Cosmetic> findCosmetic = cosmeticService.findByKeyword(keyword);

            if (findCosmetic.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.CSMT_NOT_RESULT);
            }
            List<CosmeticDto> collect = findCosmetic.stream()
                    .map(CosmeticDto::new)
                    .collect(Collectors.toList());

            return ResultDto.of(HttpStatusCode.OK, "화장품 검색 결과 가져오기 성공", collect);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }
}
