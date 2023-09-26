package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.repository.IngredientRepository;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {

    private final UserRepository user_repository;
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

    /**
     * 선호/기피성분 등록
     */
    @Transactional
    public void addIngrToUser(User user, boolean isPreference, List<Long> newIngrId) {
        // 선호 성분
        if (isPreference == true) {
            List<Long> preferIngrList = user.getPreferIngrList();
            for ( Long item : newIngrId) {
                if (!preferIngrList.contains(item)) {
                    preferIngrList.add(item);
                }
            }

            user.setPreferIngrList(preferIngrList);
            user_repository.updatePreferIngr(user);
        }
        // 기피 성분
        else {
            List<Long> dislikeIngrList = user.getDislikeIngrList();
            for ( Long item : newIngrId ) {
                if (!dislikeIngrList.contains(item)) {
                    dislikeIngrList.add(item);
                }
            }

            user.setDislikeIngrList(dislikeIngrList);
            user_repository.updateDislikeIngr(user);
        }
    }

    /**
     * 선호/기피성분 삭제
     */
    @Transactional
    public String deleteIngrList(User user, boolean isPreference, List<Long> ingrIdList) {
        List<Long> preferIngrList = user.getPreferIngrList();
        List<Long> dislikeIngrList = user.getDislikeIngrList();

        if (isPreference == true) { //선호 성분
            if(preferIngrList.isEmpty()) {
                return "성분 list가 없습니다.";
            }
            else {
                for (Object item : ingrIdList) {
                    if (!(item instanceof Long)) {
                        return item + "가 정수가 아닙니다.";
                    }
                    if (!preferIngrList.contains(item)) {
                        return item + "가 존재하지 않습니다.";
                    }
                    if (ingrIdList.indexOf(item) != ingrIdList.lastIndexOf(item)) {
                        return item + "가 중복되었습니다.";
                    }
                }
                if (preferIngrList.containsAll(ingrIdList)) {
                    preferIngrList.removeAll(ingrIdList);
                    user.setPreferIngrList(preferIngrList);
                    user_repository.updatePreferIngr(user);
                    return "";
                } else {
                    return "서버 에러";
                }
            }
        } else {    //기피 성분
            if(dislikeIngrList.isEmpty()) {
                return "성분 list가 없습니다.";
            }
            else {
                for (Object item : ingrIdList) {
                    if (!(item instanceof Long)) {
                        return item + "가 정수가 아닙니다.";
                    }
                    if (!dislikeIngrList.contains(item)) {
                        return item + "가 존재하지 않습니다.";
                    }
                    if (ingrIdList.indexOf(item) != ingrIdList.lastIndexOf(item)) {
                        return item + "가 중복되어습니다.";
                    }
                }
                if (dislikeIngrList.containsAll(ingrIdList)) {
                    dislikeIngrList.removeAll(ingrIdList);
                    user.setDislikeIngrList(dislikeIngrList);
                    user_repository.updateDislikeIngr(user);
                    return "";
                } else {
                    return "서버 에러";
                }
            }
        }
    }

    /**
     * 선호/기피성분 조회
     */
    public List<Ingredient> getIngrForUser(User user, boolean isPreference) {
        if (isPreference == true) { //선호성분
            List<Long> preferIngrIds = user.getPreferIngrList();
            if (preferIngrIds.isEmpty()) return null;
            return ingr_repository.findAllById(preferIngrIds);
        } else {    //기피 성분
            List<Long> dislikeIngrIds = user.getDislikeIngrList();
            if (dislikeIngrIds.isEmpty()) return null;
            return ingr_repository.findAllById(dislikeIngrIds);
        }
    }
}
