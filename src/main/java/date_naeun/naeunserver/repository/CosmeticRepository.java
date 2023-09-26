package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.Cosmetic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CosmeticRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Cosmetic cosmetic) {
        em.persist(cosmetic);
    }

    public Cosmetic findById(Long cosmeticId) {
        return em.find(Cosmetic.class, cosmeticId);
    }

    public List<Cosmetic> findAllById(List<Long> cosmeticList) {
        List<Cosmetic> cosmetics =  em.createQuery("SELECT c FROM Cosmetic c WHERE c.id IN :cosmeticList", Cosmetic.class)
                .setParameter("cosmeticList", cosmeticList)
                .getResultList();

        if (cosmetics.isEmpty()) {
            throw new EntityNotFoundException("해당 id의 화장품을 찾을 수 없습니다.");
        }
        return cosmetics;
    }
}
