package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.Cosmetic;
import lombok.Data;

@Data
public class CosmeticComparisonDto {

    private long id;
    private String name;
    private String brand;
    private String image;
    private String price;
    private String rating;
    private String reviews;

    public CosmeticComparisonDto(Cosmetic cosmetic) {
        id = cosmetic.getId();
        name = cosmetic.getName();
        brand = cosmetic.getBrand();
        image = cosmetic.getImage();
        price = cosmetic.getPrice();
        rating = cosmetic.getRating();
        reviews = cosmetic.getReviews();
    }
}
