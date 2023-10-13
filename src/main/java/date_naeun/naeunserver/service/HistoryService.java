package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.History;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.repository.CosmeticRepository;
import date_naeun.naeunserver.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final CosmeticRepository cosmeticRepository;

    /**
     * 사용자 엔티티로 비교기록 조회
     */
    public List<History> getHistoryList(User user) {
        return historyRepository.findAllByUser(user);
    }

    /**
     * [To Do]
     * 비교기록 저장하는 메서드 수정
     * 일단 샘플로 넣어둠.
     */
    public void saveHistory(User user) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        List<Cosmetic> allById = cosmeticRepository.findAllById(list);
        History history = new History(user, allById);   // parameter: User, List<Cosmetic>
        historyRepository.save(history);
    }

}
