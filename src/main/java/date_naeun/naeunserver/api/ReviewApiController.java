package date_naeun.naeunserver.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import date_naeun.naeunserver.domain.Review;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.ResultDto;
import date_naeun.naeunserver.dto.ReviewDetailDto;
import date_naeun.naeunserver.dto.ReviewRequestDto;
import date_naeun.naeunserver.service.ReviewService;
import date_naeun.naeunserver.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false, defaultValue = "recent") String sort,
            @RequestParam(required = false, defaultValue = "0") int mytype
    ) {
        User user = getUser(headers);
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
    public ResultDto<Object> createCosmeticReview(@RequestHeader HttpHeaders headers,
                                                  @RequestBody @Valid ReviewRequestDto requestDto) {
        User user = getUser(headers);
        reviewService.addCosmeticReview(user, requestDto);
        System.out.println(requestDto);
        return ResultDto.of(HttpStatus.OK, "리뷰 작성하기 성공", null);
    }


    private User getUser(HttpHeaders headers) {
        String accessToken = headers.getFirst("accessToken");
        System.out.println(accessToken);

        Long user_id = 1L;
        return userService.findUserById(user_id);
    }

}
