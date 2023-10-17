package date_naeun.naeunserver.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.IngredientDetailDto;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.service.IngredientService;
import date_naeun.naeunserver.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class IngredientApiController {

    private final UserService user_service;
    private final IngredientService ingr_service;

    @GetMapping("/api/ingredient/search")
    public ResultDto<Map<String, Object>> ingr(@RequestParam String keyword) {
        List<Ingredient> findIngr = ingr_service.findOneIngr(keyword);
        List<IngredientDetailDto> collect = findIngr.stream()
                .map(IngredientDetailDto::new)
                .collect(Collectors.toList());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("ingredients", collect);
        return ResultDto.of(HttpStatusCode.OK, "성분 검색 결과 가져오기 성공", responseData);
    }

    /**
     * 유저별 선호/기피 성분 조회
     * getUserIngredient
     */
    @GetMapping("/api/ingredient/user")
    public ResultDto<Object> getUserIngr(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestParam boolean isPreference) {
        User user = user_service.findUserById(userDetail.getId());

        List<Ingredient> findUserIngr = ingr_service.getIngrForUser(user, isPreference);

        if (findUserIngr != null) {
            List<IngredientDetailDto> collect = findUserIngr.stream()
                    .map(IngredientDetailDto::new)
                    .collect(Collectors.toList());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("ingredients", collect);
            if(isPreference) {
                return ResultDto.of(HttpStatusCode.OK, "사용자의 선호성분 가져오기 성공", responseData);
            } else {
                return ResultDto.of(HttpStatusCode.OK, "사용자의 기피성분 가져오기 성공", responseData);
            }
        } else {
            if(isPreference) {
                return ResultDto.of(HttpStatusCode.OK, "선호 성분이 등록되지 않았습니다.", null);
            } else {
                return ResultDto.of(HttpStatusCode.OK, "기피 성분이 등록되지 않았습니다.", null);
            }
        }

    }

    /**
     * 유저별 선호/기피 성분 추가
     * updateUserIngredient
     */
    @PostMapping("/api/ingredient/user")
    public ResultDto<Object> createUserIngredient(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid AddUserIngrRequest request) {
        User user = user_service.findUserById(userDetail.getId());

        ingr_service.addIngrToUser(user, request.isPreference(), request.getAddedIngredient());
        System.out.println(request.isPreference);

        if(request.isPreference) {
            return ResultDto.of(HttpStatusCode.OK, "사용자 선호 성분 추가하기 성공", null);
        } else {
            return ResultDto.of(HttpStatusCode.OK, "사용자 기피 성분 추가하기 성공", null);
        }
    }

    /**
     * 유저별 선호/기피 성분 삭제
     */
    @PutMapping("/api/ingredient/user")
    public ResultDto<Object> deleteUserIngr(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid DeleteUserIngrRequest request) {
        User user = user_service.findUserById(userDetail.getId());

        String responseMsg = ingr_service.deleteIngrList(user, request.isPreference(), request.getDeletedIngr());

        if (responseMsg.equals("")) {
            return ResultDto.of(HttpStatusCode.OK, "삭제 성공", null);
        } else {
            return ResultDto.of(HttpStatusCode.OK, responseMsg, null);
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
