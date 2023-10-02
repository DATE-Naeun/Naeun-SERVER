package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.Cosmetic;
import lombok.Data;

import java.util.List;

@Data
public class CosmeticDetailDto {

    private Long cosmeticId;
    private String cosmeticName;
    private String brand;
    private String image;
    private String price;
//    private String rating;
//    private String reviews;
    private Boolean isUserCosmetic;
    private List<IngredientDetailDto> ingredients;

    public CosmeticDetailDto(Cosmetic cosmetic, List<IngredientDetailDto> ingrDto) {
        cosmeticId = cosmetic.getId();
        cosmeticName = cosmetic.getName();
        brand = cosmetic.getBrand();
        image = cosmetic.getImage();
        price = cosmetic.getPrice().toString();
//        this.rating = cosmetic.getRating().toString();
//        this.reviews = cosmetic.getReviews().toString();
        isUserCosmetic = Boolean.FALSE; // 일단 false로 초기화
        ingredients = ingrDto;
    }
}
