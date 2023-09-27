package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.repository.CosmeticRepository;
import date_naeun.naeunserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CosmeticService {

    private final CosmeticRepository cosmeticRepository;
    private final IngredientRepository ingredientRepository;

    /* 화장품 id로 화장품을 가져오는 메서드 */
    public Cosmetic getCosmeticInfo(Long id) {
        return cosmeticRepository.findById(id);
    }

    /* 화장품 성분 리스트를 가져오는 메서드 */
    public List<Ingredient> findIngreList(Cosmetic cosmetic) {
        // Cosmetic 엔티티로부터 성분 ID 목록을 얻음
        List<Long> ingreIds = cosmetic.getIngredientList();

        if (ingreIds.isEmpty()) return null;

        return ingredientRepository.findALlById(ingreIds);
    }
}
