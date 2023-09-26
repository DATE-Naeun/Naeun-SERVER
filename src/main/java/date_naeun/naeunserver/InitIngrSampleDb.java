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

    @PostConstruct
    public void init() { initIngr_service.dbInit();};

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitIngredientService {
        private final EntityManager em;

        public void dbInit() {
            Ingredient tea_tree =createIngr("티트리꽃/잎/줄기추출물",null,null);
            em.persist(tea_tree);
            Ingredient tea_tree_oil = createIngr("티트리잎오일",new ArrayList<>(Arrays.asList("피부 손상 방지")),null);
            em.persist(tea_tree_oil);
            Ingredient tea_tree_leaf_extract = createIngr("티트리잎추출물",new ArrayList<>(Arrays.asList("자극 완화","피부 진정-o","피지 분비 감소","트러블 진정","항염 효과")),null);
            em.persist(tea_tree_leaf_extract);
            Ingredient tea_tree_extract = createIngr("티트리추출물",new ArrayList<>(Arrays.asList("각질 감소")),null);
            em.persist(tea_tree_extract);
            Ingredient mg_glu = createIngr("마그네슘글루코네이트",null,null);
            em.persist(mg_glu);
            Ingredient mg_nit = createIngr("마그네슘나이트레이트",null,null);
            em.persist(mg_nit);
            Ingredient alu_mg = createIngr("알루미나마그네슘메타실리케이트",null,null);
            em.persist(alu_mg);
        }
    }

    static private Ingredient createIngr(String ingr_name,
                                         List<String> active_detail,
                                         List<String> harm_detail) {
        Ingredient ingr = new Ingredient();
        ingr.setIngr_name(ingr_name);
        ingr.setActive_detail(active_detail);
        ingr.setHarm_detail(harm_detail);

        return ingr;
    }
}
