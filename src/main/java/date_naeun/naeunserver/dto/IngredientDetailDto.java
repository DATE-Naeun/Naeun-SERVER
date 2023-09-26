package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.Ingredient;
import lombok.Data;

import java.util.List;

@Data
public class IngredientDetailDto {

    private Long id;
    private String ingr_name;

    private List<String> active_detail;
    private List<String> harm_detail;

    public IngredientDetailDto(Ingredient ingr) {
        id = ingr.getId();
        ingr_name = ingr.getIngr_name();
        active_detail = ingr.getActive_detail();
        harm_detail = ingr.getHarm_detail();
    }
}
