package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.repository.CosmeticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CosmeticService {

    private final CosmeticRepository cosmeticRepository;

    /* 화장품 id로 화장품을 가져오는 메서드 */
    public Cosmetic getCosmeticInfo(Long id) {
        return cosmeticRepository.findById(id);
    }
}
