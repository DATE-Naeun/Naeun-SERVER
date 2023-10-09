package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.CosmeticIngredient;
import date_naeun.naeunserver.repository.CosmeticRepository;
import date_naeun.naeunserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitCsmtIngrDb {

    private final InitCosmeticService initCosmeticService;

    @PostConstruct
    public void init() { initCosmeticService.dbInit(); }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitCosmeticService {

        private final EntityManager em;
        private final CosmeticRepository cosmeticRepository;
        private final IngredientRepository ingredientRepository;

        public void dbInit() {
            createCosmetic(1L, new ArrayList<>(Arrays.asList(1L, 6L, 8L)));
            createCosmetic(2L, new ArrayList<>(Arrays.asList(1L, 9L)));
            createCosmetic(3L, new ArrayList<>(Arrays.asList(2L, 5L)));
        }

        private final double[] weight = {12, 10, 8.2, 6.6, 5.2, 4, 3, 2.2, 1.6, 1.2, 1};

        private void createCosmetic(Long cosmeticId,
                                    List<Long> ingredientList) {

            CosmeticIngredient cosmeticIngredient;
            for (int i = 0; i < ingredientList.size(); i++) {
                cosmeticIngredient = new CosmeticIngredient(cosmeticRepository.findById(cosmeticId), ingredientRepository.find(ingredientList.get(i)), weight[i]);
                em.persist(cosmeticIngredient);
            }
        }
    }
}
