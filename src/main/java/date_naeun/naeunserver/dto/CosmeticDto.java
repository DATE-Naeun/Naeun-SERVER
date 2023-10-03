package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.Cosmetic;
import lombok.Data;

@Data
public class CosmeticDto {

    private Long cosmeticId;
    private String cosmeticName;
    private String brand;
    private String image;
    private String price;
    private String rating;
    private String reviews;

    public CosmeticDto(Cosmetic cosmetic) {
        cosmeticId = cosmetic.getId();
        cosmeticName = cosmetic.getName();
        brand = cosmetic.getBrand();
        image = cosmetic.getImage();
        price = cosmetic.getPrice();
        rating = cosmetic.getRating();
        reviews = cosmetic.getReviews();
    }
}
