package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.Review;
import date_naeun.naeunserver.domain.SkinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class ReviewRepository {

    @PersistenceContext
    private final EntityManager em;

    public Long save(Review review) {
        em.persist(review);
        return review.getId();
    }

    public Review find(Long id) {
        return em.find(Review.class, id);
    }

    public List<Review> findAll() {
        return em.createQuery("select r from Review r", Review.class)
                .getResultList();
    }

    // 화장품 id로 해당 화장품 리뷰 찾기
    public List<Review> findByCosmeticId(Long cosmeticId) {
        return em.createQuery("select r from Review r where r.cosmetic.id = :cosmeticId order by r.date DESC", Review.class)
                .setParameter("cosmeticId", cosmeticId)
                .getResultList();
    }

    // 사용자의 피부타입과 같은 리뷰 찾기
    public List<Review> findByUserSkinType(Long cosmeticId, String skinType) {
        return em.createQuery("select r from Review r where r.cosmetic.id = :cosmeticId and r.user.skinType.typeName = :skinType order by r.date desc", Review.class)
                .setParameter("cosmeticId", cosmeticId)
                .setParameter("skinType", skinType)
                .getResultList();
    }
}
