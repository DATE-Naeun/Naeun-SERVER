package date_naeun.naeunserver.api;

import date_naeun.naeunserver.config.jwt.CustomUserDetail;
import date_naeun.naeunserver.domain.Review;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.ReviewDetailDto;
import date_naeun.naeunserver.dto.ReviewRequestDto;
import date_naeun.naeunserver.service.ReviewService;
import date_naeun.naeunserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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
        User user = getUser(userDetail);
        List<Review> reviews = reviewService.getReviewByCosmeticId(user, cosmeticId, sort, mytype);

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
        return ResultDto.of(HttpStatus.OK, "화장품 리뷰 가져오기 성공", responseData);
    }

    @PostMapping("/api/cosmetic/review")
    public ResultDto<Object> createCosmeticReview(@AuthenticationPrincipal CustomUserDetail userDetail,
                                                  @RequestBody @Valid ReviewRequestDto requestDto) {
        User user = getUser(userDetail);

        if (requestDto.getRating() == null) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "별점이 없습니다.", null);
        }
        if (requestDto.getContent() == null ) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "리뷰 내용이 없습니다.", null);
        }
        if (requestDto.getTexture() == null ) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "발림성이 없습니다.", null);
        }
        if (requestDto.getRepurchase() == null ) {
            return ResultDto.of(HttpStatus.BAD_REQUEST, "재구매 의사가 없습니다.", null);
        }

        reviewService.addCosmeticReview(user, requestDto);
        System.out.println(requestDto);
        return ResultDto.of(HttpStatus.CREATED, "리뷰 작성하기 성공", null);
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

}
