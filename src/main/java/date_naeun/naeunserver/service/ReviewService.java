package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Review;
import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository review_repository;


    /**
     * review 등록
     */
    @Transactional
    public Long save(Review review) {
        review_repository.save(review);
        return review.getId();
    }

    /**
     * user, 화장품 id, 정렬기준, 피부타입으로 필터링하여 리뷰 조회
     */
    public List<Review> getReviewByCosmeticId(User user, Long cosmeticId, String sort, int myType) {
        List<Review> reviews;

        if(myType==1) {
            String skinType = user.getSkinType().getTypeName();
            reviews = review_repository.findByUserSkinType(cosmeticId, skinType);

        } else {
            reviews = review_repository.findByCosmeticId(cosmeticId);
        }

        // sort 파라미터에 따라 정렬 수행
        if ("recent".equals(sort)) {
            reviews.sort((r1, r2) -> r2.getDate().compareTo(r1.getDate()));
        } else if ("lowrating".equals(sort)) {
            // 별점이 낮은 순으로 정렬
            reviews.sort((r1, r2) -> Double.compare(r1.getRating(), r2.getRating()));
        } else if ("highrating".equals(sort)) {
            // 별점이 높은 순으로 정렬
            reviews.sort((r1, r2) -> Double.compare(r2.getRating(), r1.getRating()));
        }

        return reviews;
    }

    /**
     * ratingAvg: 평점 평균 구하기
     */
    public double calculateRatingAvg(Long cosmeticId) {
        List<Review> reviews = review_repository.findByCosmeticId(cosmeticId);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        double totalRating = 0.0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }

        return totalRating / reviews.size();
    }

    /**
     * skinTypeRanking: 해당 화장품의 리뷰를 남긴 사용자들의 가장 많은 피부타입 3가지 선정
     */
    public List<String> getSkinTypeRanking(Long cosmeticId) {
        List<Review> reviews = review_repository.findByCosmeticId(cosmeticId);

        // 피부타입별 리뷰 수를 카운트하는 맵 생성
        Map<String, Long> skinTypeCountMap = new HashMap<>();

        // 리뷰를 순회하며 피부타입 별로 카운트
        for (Review review : reviews) {
            String skinType = review.getUser().getSkinType().getTypeName();
            skinTypeCountMap.put(skinType, skinTypeCountMap.getOrDefault(skinType, 0L) + 1);
        }

        // 카운트를 기준으로 상위 3개의 피부타입을 가져오기
        List<String> top3SkinTypes = skinTypeCountMap.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(3)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());

        return top3SkinTypes;
    }

    /**
     * textureRanking: 해당 화장품의 발림성 상위 3개
     */
    public List<String> getTextureRanking(Long cosmeticId) {
        List<Review> reviews = review_repository.findByCosmeticId(cosmeticId);

        // 리뷰에서 texture 값들을 집계
        Map<String, Long> textureCounts = reviews.stream()
                .collect(Collectors.groupingBy(Review::getTexture, Collectors.counting()));

        // texture 값들을 빈도수 내림차순으로 정렬
        List<String> top3Textures = textureCounts.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return top3Textures;
    }

    /**
     * calculateRepurchaseRating: 재구매의사 비율 구하기
     */
    public double calculateRepurchaseRating(Long cosmeticId) {
        List<Review> reviews = review_repository.findByCosmeticId(cosmeticId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        long totalReviews = reviews.size();
        long repurchaseCount = reviews.stream()
                .filter(review -> "할래요".equals(review.getRepurchase()))
                .count();

        return ((double) repurchaseCount / totalReviews) * 100.0;
    }


}
