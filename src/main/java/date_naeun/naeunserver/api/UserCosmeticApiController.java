package date_naeun.naeunserver.api;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.UserCosmeticService;
import date_naeun.naeunserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 나의 화장대 API
 */
@RestController
@RequiredArgsConstructor
public class UserCosmeticApiController {

    private final UserService userService;
    private final UserCosmeticService userCosmeticService;

    /**
     * 나의 화장대 조회
     */
    @GetMapping("/api/cosmetic/user")
    public ResultDto<Object> getUserCosmetic(@RequestHeader HttpHeaders headers) {

        User user = getUser(headers);

        List<Cosmetic> findCosmetics = userCosmeticService.getCosmeticsForUser(user);

        // 엔티티 -> DTO 변환
        if (findCosmetics != null) {
            List<CosmeticDto> collect = findCosmetics.stream()
                    .map(c -> new CosmeticDto(c.getId(), c.getName(), c.getBrand(), c.getImage()))
                    .collect(Collectors.toList());
            return ResultDto.of(HttpStatus.OK, "나의 화장대 리스트 가져오기 성공", collect);
        } else {
            return ResultDto.of(HttpStatus.OK, "나의 화장대가 비어있습니다.", null);
        }
    }

    /**
     * 나의 화장대에 추가
     */
    @PostMapping("/api/cosmetic/user")
    public ResultDto<Object> createUserCosmetic(@RequestHeader HttpHeaders headers, @RequestBody @Valid AddUserCosmeticRequest request) {

        // Request Header에서 accessToken 값을 추출
        User user = getUser(headers);

        // 사용자 id와 화장품 id
        userCosmeticService.addCosmeticToUser(user, request.getCosmeticId());

        return ResultDto.of(HttpStatus.OK, "나의 화장대에 추가하기 성공", null);
    }

    /**
     * 나의 화장대에서 삭제
     */
    @PutMapping("/api/cosmetic/user")
    public ResultDto<Object> deleteUserCosmetic(@RequestHeader HttpHeaders headers, @RequestBody @Valid DeleteUserCosmeticRequest request) {

        // Request Header에서 accessToken 값을 추출
        User user = getUser(headers);

        // 사용자 id와 화장품 id 리스트
        userCosmeticService.deleteCosmeticToUser(user, request.getDeletedCosmetic());

        return ResultDto.of(HttpStatus.OK, "나의 화장대 화장품 다중 삭제 성공", null);

    }

    /**
     * To Do
     * - JWT 토큰으로 사용자 id 가져오는 로직 구현
     */
    /* access Token 으로 사용자를 받아오는 메서드 */
    private User getUser(HttpHeaders headers) {
        // Request Header에서 accessToken 값을 추출
        String accessToken = headers.getFirst("accessToken");
        System.out.println(accessToken);
        // accessToken 유효성 검사: accessToken을 검증하고 사용자 ID를 추출하는 로직 구현

        Long userId = 1L;
        return userService.findUserById(userId);        // 액세스 토큰을 사용하여 사용자 id 조회
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class CosmeticDto {
        private Long id;
        private String name;
        private String brand;
        private String image;
    }

    @Data
    static class AddUserCosmeticRequest {
        private Long cosmeticId;
    }

    @Data
    static class DeleteUserCosmeticRequest {
        private List<Long> deletedCosmetic;
    }

}
