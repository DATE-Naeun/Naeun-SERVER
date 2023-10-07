package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.UserCosmeticService;
import date_naeun.naeunserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 나의 화장대 API
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserCosmeticApiController {

    private final UserService userService;
    private final UserCosmeticService userCosmeticService;

    /**
     * 나의 화장대 조회
     */
    @GetMapping("/api/cosmetic/user")
    public ResultDto<Object> getUserCosmetic(@AuthenticationPrincipal CustomUserDetail userDetail) {

        // 현재 로그인한 유저
        User user = getUser(userDetail);

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
    public ResultDto<Object> createUserCosmetic(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid AddUserCosmeticRequest request) {

        // 현재 로그인한 유저
        User user = getUser(userDetail);

        // 사용자 id와 화장품 id
        userCosmeticService.addCosmeticToUser(user, request.getCosmeticId());

        return ResultDto.of(HttpStatus.OK, "나의 화장대에 추가하기 성공", null);
    }

    /**
     * 나의 화장대에서 삭제
     */
    @PutMapping("/api/cosmetic/user")
    public ResultDto<Object> deleteUserCosmetic(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid DeleteUserCosmeticRequest request) {

        // 현재 로그인한 유저
        User user = getUser(userDetail);

        // 사용자 id와 화장품 id 리스트
        String responseMsg = userCosmeticService.deleteCosmeticToUser(user, request.getDeletedCosmetic());

        if (responseMsg.equals("")) {
            return ResultDto.of(HttpStatus.OK, "나의 화장대 화장품 다중 삭제 성공", null);
        } else
            return ResultDto.of(HttpStatus.BAD_REQUEST, responseMsg, null);

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
