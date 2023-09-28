package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.Ingredient;
import lombok.Data;

import java.util.List;

@Data
public class IngredientDetailDto {

    private Long ingredientId;
    private String ingredientName;

    private List<String> activeDetail;
    private List<String> harmfulDetail;

    public IngredientDetailDto(Ingredient ingr) {
        ingredientId = ingr.getId();
        ingredientName = ingr.getIngr_name();
        activeDetail = ingr.getActive_detail();
        harmfulDetail = ingr.getHarm_detail();
    }
}
