package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {

    private final IngredientRepository ingr_repository;

    /**
     * 성분 등록
     */
    @Transactional
    public Long save(Ingredient ingr) {
        validateDuplicateIngr(ingr);
        ingr_repository.save(ingr);
        return ingr.getId();
    }

    private void validateDuplicateIngr(Ingredient ingr) {
        List<Ingredient> findIngr = ingr_repository.findOneIngr(ingr.getIngr_name());
        if (!findIngr.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 성분입니다.");
        }
    }

    /**
     * 전체 성분 조회
     */
    public List<Ingredient> findAllIngr() {
        return ingr_repository.findALl();
    }

    /**
     * 성분 조회
     */
    public List<Ingredient> findOneIngr(String ingr_name) {
        return ingr_repository.findOneIngr(ingr_name);
    }
}
