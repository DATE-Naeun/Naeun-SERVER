package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(RefreshToken refreshToken) {
        em.persist(refreshToken);
    }

    /* RefreshToken entity 가져오기 */
    public RefreshToken findByToken(String token) {
        return em.createQuery("select t from RefreshToken t where t.token = :token", RefreshToken.class)
                .setParameter("token", token)
                .getSingleResult();
    }

    /* 리프레시 토큰 삭제 */
    public void delete(RefreshToken refreshToken) {
        em.remove(refreshToken);
    }
}
