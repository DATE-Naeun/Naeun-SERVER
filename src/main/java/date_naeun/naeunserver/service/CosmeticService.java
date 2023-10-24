package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.History;
import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.repository.CosmeticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CosmeticService {

    private final CosmeticRepository cosmeticRepository;
    private final IngredientService ingredientService;
    private final CosmeticIngredientService cosmeticIngredientService;


    public Cosmetic createCosmeticFromPhoto(String name, String brand, String category, List<String> ingredients) {
        Cosmetic cosmetic = new Cosmetic(name, brand, "", category, "");
        if (findByKeyword(name).isEmpty()) {
            cosmeticRepository.save(cosmetic);

            // 성분 가중치 매핑
            List<Long> ingredientIds = ingredients.stream()
                    .map(i -> ingredientService.findOneIngr(i).get(0).getId())
                    .collect(toList());

            cosmeticIngredientService.createCosmeticIngredient(cosmetic.getId(), ingredientIds);
        } else {
            cosmetic = findByKeyword(name).get(0);
        }

        return cosmetic;
    }

    /* 화장품 id로 화장품을 가져오는 메서드 */
    public Cosmetic getCosmeticInfo(Long id) {
        return cosmeticRepository.findById(id);
    }

    public List<Cosmetic> findByKeyword(String keyword) {
        return cosmeticRepository.findByKeyword(keyword);
    }

    /**
     *  비교 기록에 있는 Cosmetic list 조회
     */
    public List<Cosmetic> getCosmeticsByHistory(History history) {
        return cosmeticRepository.findCosmeticsByHistory(history);
    }

    /**
     * 해당 skinType 이 가장 많이 비교했던 화장품 3개 조회
     */
    public List<Cosmetic> getTop3(SkinType skinType) {
        return cosmeticRepository.findRankingTop3(skinType);
    }
}
