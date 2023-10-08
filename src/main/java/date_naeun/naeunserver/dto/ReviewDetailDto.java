package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.Review;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ReviewDetailDto {
    private Long reviewId;
    private String userName;
    private String skinType;
    private double rating;
    private Date date;
    private String content;
    private String texture;
    private String repurchase;
    private List<String> image;

    public ReviewDetailDto(Review review) {
        reviewId = review.getId();
        userName = review.getUser().getName();
        skinType = review.getUser().getSkinType().getTypeName();
        rating = review.getRating();
        date = review.getDate();
        content = review.getContent();
        texture = review.getTexture();
        repurchase = review.getRepurchase();
        image = review.getImage();
    }
}
