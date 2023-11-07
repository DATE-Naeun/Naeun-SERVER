package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.History;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;

    /**
     * 사용자 엔티티로 비교기록 조회
     */
    public List<History> getHistoryList(User user) {
        return historyRepository.findAllByUser(user);
    }

    public void saveHistory(User user, List<Cosmetic> cosmetics) {
        History history = new History(user, cosmetics);
        historyRepository.save(history);
    }

}
