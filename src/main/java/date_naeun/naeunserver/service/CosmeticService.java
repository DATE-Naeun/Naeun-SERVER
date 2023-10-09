package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
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
        cosmeticRepository.save(cosmetic);

        // 성분 가중치 매핑
        List<Long> ingredientIds = ingredients.stream()
                .map(i -> ingredientService.findOneIngr(i).get(0).getId())
                .collect(toList());

        cosmeticIngredientService.createCosmeticIngredient(cosmetic.getId(), ingredientIds);

        return cosmetic;
    }

    /* 화장품 id로 화장품을 가져오는 메서드 */
    public Cosmetic getCosmeticInfo(Long id) {
        return cosmeticRepository.findById(id);
    }

    public List<Cosmetic> findByKeyword(String keyword) {
        return cosmeticRepository.findByKeyword(keyword);
    }

    /* 화장품 id 리스트로 화장품을 가져오는 메서드 */
    public List<Cosmetic> findByIdList(List<Long> cosmeticList) {
        return cosmeticRepository.findAllById(cosmeticList);
    }
}
