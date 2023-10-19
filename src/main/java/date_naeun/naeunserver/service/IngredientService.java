package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.exception.ApiErrorException;
import date_naeun.naeunserver.exception.ApiErrorStatus;
import date_naeun.naeunserver.exception.ApiErrorWithItemException;
import date_naeun.naeunserver.repository.IngredientRepository;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return ingr_repository.findAll();
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
                } else {
                    throw new ApiErrorWithItemException(ApiErrorStatus.INGR_DUPLICATED_ID, item);
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
                } else {
                    throw new ApiErrorWithItemException(ApiErrorStatus.INGR_DUPLICATED_ID, item);
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
    public void deleteIngrList(User user, boolean isPreference, List<Long> ingrIdList) {
        List<Long> preferIngrList = user.getPreferIngrList();
        List<Long> dislikeIngrList = user.getDislikeIngrList();

        if (isPreference == true) { //선호 성분
            if(preferIngrList.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.INGR_LIST_NOT_EXIST);
            }
            else {
                for (Object item : ingrIdList) {
                    if (!(item instanceof Long)) {
                        throw new ApiErrorWithItemException(ApiErrorStatus.INGR_NOT_INTEGER, item);
                    }
                    if (!preferIngrList.contains(item)) {
                        throw new ApiErrorWithItemException(ApiErrorStatus.INGR_ID_NOT_EXIST, item);
                    }
                    if (ingrIdList.indexOf(item) != ingrIdList.lastIndexOf(item)) {
                        throw new ApiErrorWithItemException(ApiErrorStatus.INGR_DUPLICATED_ID, item);
                    }
                }
                if (preferIngrList.containsAll(ingrIdList)) {
                    preferIngrList.removeAll(ingrIdList);
                    user.setPreferIngrList(preferIngrList);
                    user_repository.updatePreferIngr(user);
                }
            }
        } else {    //기피 성분
            if(dislikeIngrList.isEmpty()) {
                throw new ApiErrorException(ApiErrorStatus.INGR_LIST_NOT_EXIST);
            }
            else {
                for (Object item : ingrIdList) {
                    if (!(item instanceof Long)) {
                        throw new ApiErrorWithItemException(ApiErrorStatus.INGR_NOT_INTEGER, item);
                    }
                    if (!dislikeIngrList.contains(item)) {
                        throw new ApiErrorWithItemException(ApiErrorStatus.INGR_ID_NOT_EXIST, item);
                    }
                    if (ingrIdList.indexOf(item) != ingrIdList.lastIndexOf(item)) {
                        throw new ApiErrorWithItemException(ApiErrorStatus.INGR_DUPLICATED_ID, item);
                    }
                }
                if (dislikeIngrList.containsAll(ingrIdList)) {
                    dislikeIngrList.removeAll(ingrIdList);
                    user.setDislikeIngrList(dislikeIngrList);
                    user_repository.updateDislikeIngr(user);
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

    /* 화장품 성분 리스트를 가져오는 메서드 */
    public List<Ingredient> findIngrList(Cosmetic cosmetic) {
        // Cosmetic 엔티티로부터 성분 ID 목록을 얻음
        List<Long> ingreIds = cosmetic.getIngredientList();

        if (ingreIds.isEmpty()) return null;

        return ingr_repository.findAllById(ingreIds);
    }
}
