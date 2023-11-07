package date_naeun.naeunserver.repository;

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
public class SkinTypeRepository {

    @PersistenceContext
    private final EntityManager em;

    public Long save(SkinType skinType) {
        em.persist(skinType);
        return skinType.getId();
    }

    public SkinType find(Long id) {
        return em.find(SkinType.class,id);
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

    public SkinType findSkinType(String typeName) {
        List<SkinType> result = em.createQuery("select s from SkinType s where s.typeName = :typeName", SkinType.class)
                .setParameter("typeName", typeName)
                .getResultList();

        if (result.isEmpty()){
            return null;
        }
        return result.get(0);
    }
}
