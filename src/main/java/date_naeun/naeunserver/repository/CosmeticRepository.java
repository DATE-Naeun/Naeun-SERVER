package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.History;
import date_naeun.naeunserver.domain.SkinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

    public List<Cosmetic> findByKeyword(String name) {
        return em.createQuery("select c from Cosmetic c where c.name LIKE CONCAT('%', :name, '%') OR c.brand LIKE CONCAT('%', :name, '%')", Cosmetic.class)
                .setParameter("name", name)
                .getResultList();
    }

    /**
     * 비교기록에 있는 화장품 리스트 조회
     */
    public List<Cosmetic> findCosmeticsByHistory(History history) {
        List<Cosmetic> cosmetics = em.createQuery("select hc.cosmetic from HistoryCosmetic hc where hc.history = :history", Cosmetic.class)
                .setParameter("history", history)
                .getResultList();
        if (cosmetics == null || cosmetics.isEmpty()) {
            return new ArrayList<>();
        }
        return cosmetics;
    }

    /**
     * skinType으로 랭킹3 화장품 리스트 조회
     */
    public List<Cosmetic> findRankingTop3(SkinType skinType) {
        return em.createQuery("select hc.cosmetic from HistoryCosmetic hc where hc.history.skinType = :skinType " +
                        "GROUP BY hc.cosmetic " +
                        "ORDER BY count(hc.cosmetic) DESC", Cosmetic.class)
                .setParameter("skinType", skinType)
                .setMaxResults(3)
                .getResultList();
    }
}
