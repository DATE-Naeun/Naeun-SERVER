package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.dto.SkinTypeDetailDto;
import date_naeun.naeunserver.service.SkinTypeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SkinTypeApiController {

    private final SkinTypeService skinType_service;

    @GetMapping("/api/skintype/detail")
    public Result skinType(@RequestParam String skinType) {
        List<SkinType> findSkinType = skinType_service.findOneSkinType(skinType);
        List<SkinTypeDetailDto> collect = findSkinType.stream()
                .map(SkinTypeDetailDto::new)
                .collect(Collectors.toList());
        return new Result(collect);
    }


    @Data @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
