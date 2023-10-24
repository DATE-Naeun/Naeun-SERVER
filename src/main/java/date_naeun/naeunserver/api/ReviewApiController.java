package date_naeun.naeunserver.api;

import date_naeun.naeunserver.exception.ApiErrorException;
import date_naeun.naeunserver.exception.ApiErrorStatus;
import date_naeun.naeunserver.exception.AuthErrorException;
import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Review;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.ReviewDetailDto;
import date_naeun.naeunserver.dto.ReviewRequestDto;
import date_naeun.naeunserver.exception.HttpStatusCode;
import date_naeun.naeunserver.service.ReviewService;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/api/cosmetic/review/{cosmeticId}")
    public ResultDto<Map<String, Object>> getReviewsForCosmetic(
            @PathVariable Long cosmeticId,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(required = false, defaultValue = "recent") String sort,
            @RequestParam(required = false, defaultValue = "0") int mytype
    ) {
        try {
            User user = userService.findUserById(userDetail.getId());
            List<Review> reviews = reviewService.getReviewByCosmeticId(user, cosmeticId, sort, mytype);

            if (reviews.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.REVIEW_NOT_EXIST );
            }

            double ratingAvg = reviewService.calculateRatingAvg(cosmeticId);
            List<String> skinTypeRanking = reviewService.getSkinTypeRanking(cosmeticId);
            List<String> textureRanking = reviewService.getTextureRanking(cosmeticId);
            double repurchaseRate = reviewService.calculateRepurchaseRating(cosmeticId);

            List<ReviewDetailDto> collect = reviews.stream()
                    .map(ReviewDetailDto::new)
                    .collect(Collectors.toList());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("cosmeticId", cosmeticId);
            responseData.put("ratingAvg", ratingAvg);
            responseData.put("skinTypeRanking", skinTypeRanking);
            responseData.put("textureRanking", textureRanking);
            responseData.put("repurchaseRate", repurchaseRate);
            responseData.put("reviews", collect);
            return ResultDto.of(HttpStatusCode.OK, "화장품 리뷰 가져오기 성공", responseData);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

    @PostMapping("/api/cosmetic/review")
    public ResultDto<Object> createCosmeticReview(@AuthenticationPrincipal CustomUserDetail userDetail,
                                                  @RequestBody @Valid ReviewRequestDto requestDto) {
        try {
            User user = userService.findUserById(userDetail.getId());

            if (requestDto.getRating() == null) {
                throw new ApiErrorException(ApiErrorStatus.REVIEW_NOT_RATING);
            }
            if (requestDto.getContent() == null) {
                throw new ApiErrorException(ApiErrorStatus.REVIEW_NOT_CONTENT);
            }
            if (requestDto.getTexture() == null) {
                throw new ApiErrorException(ApiErrorStatus.REVIEW_NOT_TEXTURE);
            }
            if (requestDto.getRepurchase() == null) {
                throw new ApiErrorException(ApiErrorStatus.REVIEW_NOT_REPURCHASE);
            }

            reviewService.addCosmeticReview(user, requestDto);
            System.out.println(requestDto);
            return ResultDto.of(HttpStatusCode.CREATED, "리뷰 작성하기 성공", null);

        } catch (AuthErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (ApiErrorException e) {
            return ResultDto.of(e.getCode(), e.getErrorMsg(), null);
        } catch (Exception e) {
            return ResultDto.of(HttpStatusCode.INTERNAL_SERVER_ERROR, "서버 에러", null);
        }
    }

}
