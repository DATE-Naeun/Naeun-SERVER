package date_naeun.naeunserver.service;

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
     * 사용자 id로 비교기록 조회
     */
    public List<History> getHistoryById(User user) {
        // user id로 사용자의 비교기록을 찾음.
        // User 엔티티로부터 비교기록 리스트를 얻음
        List<Long> historyIds = user.getHistoryList();

        // 비교기록이 없는 경우 null 반환
        if (historyIds.isEmpty()) return null;

        return historyRepository.findAllById(historyIds);
    }
}
