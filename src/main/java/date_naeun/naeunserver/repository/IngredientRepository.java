package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class IngredientRepository {

    @PersistenceContext
    private final EntityManager em;

    public Long save(Ingredient ingr) {
        em.persist(ingr);
        return ingr.getId();
    }

    public Ingredient find(Long id) {
        return em.find(Ingredient.class, id);
    }

    public List<Ingredient> findALl() {
        return em.createQuery("select i from Ingredient i", Ingredient.class)
                .getResultList();
    }

    public List<Ingredient> findOneIngr(String name) {
        return em.createQuery("select i from Ingredient i where i.ingr_name LIKE CONCAT('%', :name, '%')", Ingredient.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Ingredient> findALlById(List<Long> ingreList) {
        List<Ingredient> ingredients =  em.createQuery("SELECT i FROM Ingredient i WHERE i.id IN :ingreList", Ingredient.class)
                .setParameter("ingreList", ingreList)
                .getResultList();

        if (ingredients.isEmpty()) {
            throw new EntityNotFoundException("해당 id의 성분을 찾을 수 없습니다.");
        }
        return ingredients;

    }

}
