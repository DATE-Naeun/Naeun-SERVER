package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.exception.ApiErrorStatus;
import date_naeun.naeunserver.exception.ApiErrorWithItemException;
import date_naeun.naeunserver.repository.CosmeticRepository;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 나의 화장대 서비스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCosmeticService {

    private final UserRepository userRepository;
    private final CosmeticRepository cosmeticRepository;

    /**
     * 나의 화장대 조회
     * - 나의 화장대에 있는 화장품 id 목록으로 해당 화장품들 검색
     */
    public List<Cosmetic> getCosmeticsForUser(User user) {

        // User 엔티티로부터 화장품 ID 목록을 얻음
        List<Long> cosmeticIds = user.getUserCosmeticList();

        // 나의 화장대가 비어있는 경우 빈 리스트 반환
        if (cosmeticIds.isEmpty()) return new ArrayList<>();

        // 화장품 ID 목록을 사용하여 단일 쿼리로 화장품 정보를 가져옴
        return cosmeticRepository.findAllById(cosmeticIds);
    }

    /**
     * 나의 화장대에 추가
     * - 사용자와 새로 추가할 화장품 id를 받아서 CosmeticList에 추가
     */
    @Transactional
    public void addCosmeticToUser(User user, Long newCosmeticId) {
        List<Long> cosmeticList = user.getUserCosmeticList();

        // 이미 있는 화장품 id인지 확인 후 추가
        if (!cosmeticList.contains(newCosmeticId)) {
            cosmeticList.add(newCosmeticId);
            user.setUserCosmeticList(cosmeticList);
            userRepository.updateUserCosmetic(user); // 변경 내용을 db에 반영
        }
    }

    /**
     * 나의 화장대에서 화장품 삭제
     * - 사용자와 삭제할 화장품 리스트를 받아서 삭제
     */
    @Transactional
    public void deleteCosmeticToUser(User user, List<Object> cosmeticIdList) {
        List<Long> cosmeticList= user.getUserCosmeticList();

        List<Long> cosmeticLongIdList = cosmeticIdList.stream()
                .map(this::convertToLongOrThrow)
                .collect(Collectors.toList());

        // 나의 화장대에 있는 id인지 확인
        cosmeticLongIdList.forEach(itemId -> {
            if (!cosmeticList.contains(itemId)) {
                throw new ApiErrorWithItemException(ApiErrorStatus.NOT_EXIST, itemId);
            }
        });

        // 중복되는 id 확인
        Set<Long> uniqueIds = new HashSet<>();
        for (Long itemId : cosmeticLongIdList) {
            if (!uniqueIds.add(itemId)) {
                throw new ApiErrorWithItemException(ApiErrorStatus.DUPLICATED_ID, itemId);
            }
        }

        // Remove the cosmetics if all checks pass
        cosmeticList.removeAll(cosmeticLongIdList);
        user.setUserCosmeticList(cosmeticList);
        userRepository.updateUserCosmetic(user);  // Update the database
    }

    private Long convertToLongOrThrow(Object obj) {
        if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        } else {
            throw new ApiErrorWithItemException(ApiErrorStatus.NOT_INTEGER, obj);
        }
    }
}
