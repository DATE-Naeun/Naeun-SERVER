package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.SkinTypeDetailDto;
import date_naeun.naeunserver.service.SkinTypeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SkinTypeApiController {

    private final SkinTypeService skinType_service;

    @GetMapping("/api/skintype/detail")
    public ResultDto<List<SkinTypeDetailDto>> skinType(@RequestParam String skinType) {
        List<SkinType> findSkinType = skinType_service.findOneSkinType(skinType.toUpperCase());
        List<SkinType> findAllSkinType = skinType_service.findAllSkinType();

        if(findAllSkinType.isEmpty()) {
            return ResultDto.of(HttpStatus.BAD_REQUEST,"스킨타입이 없습니다.", null);
        }

        // 모든 스킨타입의 이름을 추출해 목록 생성
        List<String> allSkinTypeNames = findAllSkinType.stream()
                .map(SkinType::getTypeName)
                .collect(Collectors.toList());

        // 입력된 스킨타입이 allSkinTypeNames 목록에 있는지 확인
        boolean validSkinType = allSkinTypeNames.stream()
                .anyMatch(skinType::equalsIgnoreCase);

        if (!validSkinType) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "유효하지 않은 스킨타입입니다.", null);
        }


        List<SkinTypeDetailDto> collect = findSkinType.stream()
                .map(SkinTypeDetailDto::new)
                .collect(Collectors.toList());
        return ResultDto.of(HttpStatus.OK, "피부타입 상세 정보 가져오기 성공", collect);
    }


    @Data @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
