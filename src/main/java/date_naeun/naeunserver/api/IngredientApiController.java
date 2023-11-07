package date_naeun.naeunserver.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import date_naeun.naeunserver.exception.*;
import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.IngredientDetailDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.service.IngredientService;
import date_naeun.naeunserver.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class IngredientApiController {

    private final UserService user_service;
    private final IngredientService ingr_service;

    @GetMapping("/api/ingredient/search")
    public ResultDto<Map<String, Object>> ingr(@RequestParam String keyword) {
        try {
            List<Ingredient> findIngr = ingr_service.findOneIngr(keyword);

            if (findIngr.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.INGR_NOT_RESULT);
            }
            List<IngredientDetailDto> collect = findIngr.stream()
                    .map(IngredientDetailDto::new)
                    .collect(Collectors.toList());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("ingredients", collect);
            return ResultDto.of(HttpStatusCode.OK, "성분 검색 결과 가져오기 성공", responseData);
        } catch (ApiErrorException e){
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    /**
     * 유저별 선호/기피 성분 조회
     * getUserIngredient
     */
    @GetMapping("/api/ingredient/user")
    public ResultDto<Object> getUserIngr(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestParam boolean isPreference) {
        try {
            User user = user_service.findUserById(userDetail.getId());

            List<Ingredient> findUserIngr = ingr_service.getIngrForUser(user, isPreference);

            if (findUserIngr != null) {
                List<IngredientDetailDto> collect = findUserIngr.stream()
                        .map(IngredientDetailDto::new)
                        .collect(Collectors.toList());

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("ingredients", collect);
                if (isPreference) {
                    return ResultDto.of(HttpStatusCode.OK, "사용자의 선호성분 가져오기 성공", responseData);
                } else {
                    return ResultDto.of(HttpStatusCode.OK, "사용자의 기피성분 가져오기 성공", responseData);
                }
            } else {
                if (isPreference) {
                    throw new ApiErrorException(ApiErrorStatus.INGR_PREFER_NOT_EXIST);
                } else {
                    throw new ApiErrorException(ApiErrorStatus.INGR_DISLIKE_NOT_EXIST);
                }
            }
        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }

    }

    /**
     * 유저별 선호/기피 성분 추가
     * updateUserIngredient
     */
    @PostMapping("/api/ingredient/user")
    public ResultDto<Object> createUserIngredient(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid AddUserIngrRequest request) {
        try {
            User user = user_service.findUserById(userDetail.getId());

            List<Ingredient> findAll = ingr_service.findAllIngr();
            List<Long> allIngrIds = findAll.stream()
                    .map(Ingredient::getId)
                    .collect(Collectors.toList());

            // 성분 list가 없는 경우
            if (request.addedIngredient.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.INGR_LIST_NOT_EXIST);
            }

            for (Long id : request.getAddedIngredient()) {
                // 입력받은 id가 정수가 아닌 경우
                if (!isPositiveInteger(id)) {
                    throw new ApiErrorWithItemException(ApiErrorStatus.INGR_NOT_INTEGER, id);
                }

                if (!allIngrIds.contains(id)) {
                    throw new ApiErrorWithItemException(ApiErrorStatus.INGR_ID_NOT_EXIST, id);
                }
            }

            ingr_service.addIngrToUser(user, request.isPreference(), request.getAddedIngredient());
            System.out.println(request.isPreference);

            if (request.isPreference) {
                return ResultDto.of(HttpStatusCode.CREATED, "사용자 선호 성분 추가하기 성공", null);
            } else {
                return ResultDto.of(HttpStatusCode.CREATED, "사용자 기피 성분 추가하기 성공", null);
            }
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
    private boolean isPositiveInteger(Long value) {
        // Long 데이터가 null 또는 0보다 작거나 같으면 정수가 아닌 것으로 판별
        return value != null && value > 0;
    }

    /**
     * 유저별 선호/기피 성분 삭제
     */
    @PutMapping("/api/ingredient/user")
    public ResultDto<Object> deleteUserIngr(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid DeleteUserIngrRequest request) {
        try {
            User user = user_service.findUserById(userDetail.getId());

            ingr_service.deleteIngrList(user, request.isPreference(),request.getDeletedIngr());

            return ResultDto.of(HttpStatusCode.OK, "삭제 성공", null);
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
    static class AddUserIngrRequest {
        @Setter
        @JsonProperty("isPreference")
        private boolean isPreference;
        private List<Long> addedIngredient;
    }

    @Data
    static class DeleteUserIngrRequest {
        @JsonProperty("isPreference")
        private boolean isPreference;
        private List<Long> deletedIngr;
    }
}
