package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.History;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
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
     * history List : id -> History 엔티티로 반환
     */
    public List<History> findAllById(List<Long> historyList) {
        List<History> histories = em.createQuery("select h from History h where h.id in :historyList", History.class)
                .setParameter("historyList", historyList)
                .getResultList();

        if (histories.isEmpty()) {
            throw new EntityNotFoundException("해당 id의 비교기록을 찾을 수 없습니다.");
        }

        return histories;
    }
}
