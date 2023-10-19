package date_naeun.naeunserver.api;

import date_naeun.naeunserver.exception.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

        try {
            // 현재 로그인한 유저
            User user = userService.findUserById(userDetail.getId());
            List<Cosmetic> findCosmetics = userCosmeticService.getCosmeticsForUser(user);

            // 엔티티 -> DTO 변환
            List<CosmeticDto> collect = findCosmetics.stream()
                    .map(c -> new CosmeticDto(c.getId(), c.getName(), c.getBrand(), c.getImage()))
                    .collect(Collectors.toList());
            return ResultDto.of(HttpStatusCode.OK, "나의 화장대 리스트 가져오기 성공", collect);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 나의 화장대에 추가
     */
    @PostMapping("/api/cosmetic/user")
    public ResultDto<Object> createUserCosmetic(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid AddUserCosmeticRequest request) {

        try {
            if (request.getCosmeticId() == null) throw new ApiErrorException(ApiErrorStatus.ID_NOT_EXIST);

            // 현재 로그인한 유저
            User user = userService.findUserById(userDetail.getId());
            // 사용자 id와 화장품 id
            userCosmeticService.addCosmeticToUser(user, request.getCosmeticId());

            return ResultDto.of(HttpStatusCode.CREATED, "나의 화장대에 추가하기 성공", null);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 나의 화장대에서 삭제
     */
    @PutMapping("/api/cosmetic/user")
    public ResultDto<Object> deleteUserCosmetic(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid DeleteUserCosmeticRequest request) {

        try {
            List<Object> itemsToDelete = request.getDeletedCosmetic();

            if (itemsToDelete == null || itemsToDelete.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.LIST_NOT_EXIST);
            }

            // 현재 로그인한 유저
            User user = userService.findUserById(userDetail.getId());

            // 사용자 id와 화장품 id 리스트
            userCosmeticService.deleteCosmeticToUser(user, itemsToDelete);

            return ResultDto.of(HttpStatusCode.OK, "나의 화장대 화장품 다중 삭제 성공", null);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorWithItemException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
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
        private List<Object> deletedCosmetic;
    }

}
