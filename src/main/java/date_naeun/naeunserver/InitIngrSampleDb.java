package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.Ingredient;
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
public class InitIngrSampleDb {

    private final InitIngredientService initIngr_service;

//    @PostConstruct
//    public void init() { initIngr_service.dbInit(); }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitIngredientService {
        private final EntityManager em;

        public void dbInit() {
            Ingredient tea_tree =createIngr("티트리꽃/잎/줄기추출물",null,null);
            em.persist(tea_tree);
            Ingredient tea_tree_oil = createIngr("티트리잎오일",new ArrayList<>(Arrays.asList(1L)),null);
            em.persist(tea_tree_oil);
            Ingredient tea_tree_leaf_extract = createIngr("티트리잎추출물",new ArrayList<>(Arrays.asList(11L, 16L, 10L, 13L, 12L)),null);
            em.persist(tea_tree_leaf_extract);
            Ingredient tea_tree_extract = createIngr("티트리추출물",new ArrayList<>(Arrays.asList(9L)),null);
            em.persist(tea_tree_extract);
            Ingredient mg_glu = createIngr("마그네슘글루코네이트",null,null);
            em.persist(mg_glu);
            Ingredient mg_nit = createIngr("마그네슘나이트레이트",null,null);
            em.persist(mg_nit);
            Ingredient alu_mg = createIngr("알루미나마그네슘메타실리케이트",null,null);
            em.persist(alu_mg);
            Ingredient gl = createIngr("글리세린",new ArrayList<>(Arrays.asList(1L, 2L, 6L, 18L)),null);
            em.persist(gl);
            Ingredient cb = createIngr("시어버터",new ArrayList<>(Arrays.asList(1L, 7L, 15L)),new ArrayList<>(Arrays.asList(6L)));
            em.persist(cb);
        }
    }

    static private Ingredient createIngr(String ingr_name,
                                         List<Long> active_detail_id,
                                         List<Long> harm_detail_id) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngr_name(ingr_name);
        ingredient.setActive_detail_id(active_detail_id);
        ingredient.setHarm_detail_id(harm_detail_id);

        return ingredient;
    }
}