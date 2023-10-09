package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.CosmeticIngredient;
import date_naeun.naeunserver.repository.CosmeticIngredientRepository;
import date_naeun.naeunserver.repository.CosmeticRepository;
import date_naeun.naeunserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CosmeticIngredientService {

    private final CosmeticIngredientRepository cosmeticIngredientRepository;
    private final CosmeticRepository cosmeticRepository;
    private final IngredientRepository ingredientRepository;

    private final double[] weight = {12, 10, 8.2, 6.6, 5.2, 4, 3, 2.2, 1.6, 1.2, 1};

    public void createCosmeticIngredient(Long cosmeticId,
                                List<Long> ingredientList) {

        CosmeticIngredient cosmeticIngredient;
        for (int i = 0; i < ingredientList.size(); i++) {
            if (i < 10) cosmeticIngredient = new CosmeticIngredient(cosmeticRepository.findById(cosmeticId), ingredientRepository.find(ingredientList.get(i)), weight[i]);
            else cosmeticIngredient = new CosmeticIngredient(cosmeticRepository.findById(cosmeticId), ingredientRepository.find(ingredientList.get(i)), 1.0);

            cosmeticIngredientRepository.save(cosmeticIngredient);
        }
    }
}
