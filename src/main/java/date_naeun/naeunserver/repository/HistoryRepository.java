package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.History;
import date_naeun.naeunserver.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistoryRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(History history) {
        em.persist(history);
    }

    /**
     * User로 History 조회
     */
    public List<History> findAllByUser(User user) {
        List<History> histories = em.createQuery("select h from History h where h.user = :user", History.class)
                .setParameter("user", user)
                .getResultList();

        if (histories == null || histories.isEmpty()) {
            return new ArrayList<>();
        }
        return histories;
    }
}
