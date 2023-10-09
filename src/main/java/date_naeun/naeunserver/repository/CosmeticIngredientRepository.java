package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.CosmeticIngredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class CosmeticIngredientRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(CosmeticIngredient cosmeticIngredient) {
        em.persist(cosmeticIngredient);
    }
}
