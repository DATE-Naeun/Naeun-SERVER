package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.dto.IngredientDetailDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.IngredientService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class IngredientApiController {

    private final IngredientService ingr_service;

    @GetMapping("/api/ingredient/search")
    public ResultDto<List<IngredientDetailDto>> ingr(@RequestParam String keyword) {
        List<Ingredient> findIngr = ingr_service.findOneIngr(keyword);
        List<IngredientDetailDto> collect = findIngr.stream()
                .map(IngredientDetailDto::new)
                .collect(Collectors.toList());
        return ResultDto.of(HttpStatus.OK, "성분 검색 결과 가져오기 성공", collect);
    }

    @Data @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
