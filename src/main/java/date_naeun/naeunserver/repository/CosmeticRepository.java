package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.SkinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
     * 인기 화장품 랭킹 3
     */
    public List<Cosmetic> findRankingTop3(Long userSkinType) {
        String jpqlQuery = "SELECT c\n" +
                "FROM user_table u WHERE u.skinType = :userSkinType\n" +
                "JOIN History h ON h.id IN u.historyList\n" +
                "JOIN Cosmetic c ON c.id IN elements(h.cosmeticList)\n" +
                "GROUP BY c.id\n" +
                "ORDER BY count(c.id) DESC";

        return em.createQuery(jpqlQuery, Cosmetic.class)
                .setParameter("userSkinType", userSkinType)
                .setMaxResults(3)
                .getResultList();
    }
}
