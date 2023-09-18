package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.SkinType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SkinTypeRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(SkinType skinType) {
        em.persist(skinType);
    }

    public List<SkinType> findAll() {
        return em.createQuery("select s from SkinType s", SkinType.class)
                .getResultList();
    }

    public List<SkinType> findOneSkinType(String typeName) {
        return em.createQuery("select s from SkinType s where s.typeName = :typeName", SkinType.class)
                .setParameter("typeName", typeName)
                .getResultList();
    }
}
