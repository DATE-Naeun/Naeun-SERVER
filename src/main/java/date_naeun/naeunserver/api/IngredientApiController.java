package date_naeun.naeunserver.api;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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
        List<Ingredient> findIngr = ingr_service.findOneIngr(keyword);

        if (findIngr.isEmpty()) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "검색 결과가 없습니다.", null);
        }

        List<IngredientDetailDto> collect = findIngr.stream()
                .map(IngredientDetailDto::new)
                .collect(Collectors.toList());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("ingredients", collect);
        return ResultDto.of(HttpStatus.OK, "성분 검색 결과 가져오기 성공", responseData);
    }

    /**
     * 유저별 선호/기피 성분 조회
     * getUserIngredient
     */
    @GetMapping("/api/ingredient/user")
    public ResultDto<Object> getUserIngr(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestParam boolean isPreference) {
        User user = getUser(userDetail);

        List<Ingredient> findUserIngr = ingr_service.getIngrForUser(user, isPreference);

        if (findUserIngr != null) {
            List<IngredientDetailDto> collect = findUserIngr.stream()
                    .map(IngredientDetailDto::new)
                    .collect(Collectors.toList());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("ingredients", collect);
            if(isPreference) {
                return ResultDto.of(HttpStatus.OK, "사용자의 선호성분 가져오기 성공", responseData);
            } else {
                return ResultDto.of(HttpStatus.OK, "사용자의 기피성분 가져오기 성공", responseData);
            }
        } else {
            if(isPreference) {
                return ResultDto.of(HttpStatus.OK, "선호 성분이 등록되지 않았습니다.", null);
            } else {
                return ResultDto.of(HttpStatus.OK, "기피 성분이 등록되지 않았습니다.", null);
            }
        }

    }

    /**
     * 유저별 선호/기피 성분 추가
     * updateUserIngredient
     */
    @PostMapping("/api/ingredient/user")
    public ResultDto<Object> createUserIngredient(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody @Valid AddUserIngrRequest request) {
        User user = getUser(userDetail);
        List<Ingredient> findAll = ingr_service.findAllIngr();

        List<Long> allIngrIds = findAll.stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList());

        // 성분 ID 리스트에 중복된 값이 있는지 검사
        Set<Long> uniqueIds = new HashSet<>(request.getAddedIngredient());
        if (uniqueIds.size() != request.getAddedIngredient().size()) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "성분 ID가 중복되었습니다.", null);
        }

        // 성분 list가 없는 경우
        if (request.addedIngredient.isEmpty()) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "성분 list가 없습니다.", null);
        }

        for (Long id : request.getAddedIngredient()) {
            // 입력받은 id가 정수가 아닌 경우
            if(!isPositiveInteger(id)) {
                String errorMessage = id + "가 정수가 아닙니다.";
                return ResultDto.of(HttpStatus.BAD_REQUEST, errorMessage, null);
            }

            if(!allIngrIds.contains(id)) {
                String errorMessage = id + "가 존재하지 않습니다.";
                return ResultDto.of(HttpStatus.BAD_REQUEST, errorMessage, null);
            }
        }

        ingr_service.addIngrToUser(user, request.isPreference(), request.getAddedIngredient());
        System.out.println(request.isPreference);

        if(request.isPreference) {
            return ResultDto.of(HttpStatus.OK, "사용자 선호 성분 추가하기 성공", null);
        } else {
            return ResultDto.of(HttpStatus.OK, "사용자 기피 성분 추가하기 성공", null);
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
        User user = getUser(userDetail);

        String responseMsg = ingr_service.deleteIngrList(user, request.isPreference(), request.getDeletedIngr());

        if (responseMsg.equals("")) {
            return ResultDto.of(HttpStatus.OK, "삭제 성공", null);
        } else {
            return ResultDto.of(HttpStatus.BAD_REQUEST, responseMsg, null);
        }
    }

    /**
     * JWT 토큰으로 사용자를 받아오는 메서드
     */
    private User getUser(CustomUserDetail userDetail) {
        if (userDetail == null) {
            throw new EntityNotFoundException("해당 토큰으로 사용자를 조회할 수 없습니다.");
        }
        // accessToken 검증 후 생성된 userDetail의 id로 user 찾아서 생성
        return user_service.findUserById(userDetail.getId());
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
