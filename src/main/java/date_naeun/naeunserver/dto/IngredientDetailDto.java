package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.ActiveDetail;
import date_naeun.naeunserver.domain.HarmfulDetail;
import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.repository.IngredientDetailRepository;
import lombok.Data;

import java.util.List;

@Data
public class IngredientDetailDto {

    private Long ingredientId;
    private String ingredientName;

    // TO DO: List<Long> -> List<String>으로 바꾸기(id 값 리스트로 detail 이름 리스트 가져오기)
    private List<Long> active_detail;
    private List<Long> harm_detail;

    public IngredientDetailDto(Ingredient ingr) {
        id = ingr.getId();
        ingr_name = ingr.getIngr_name();
        active_detail = ingr.getActive_detail_id();
        harm_detail = ingr.getHarm_detail_id();
    }
}
