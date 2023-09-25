package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.repository.CosmeticRepository;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

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

        // 나의 화장대가 비어있는 경우 null 반환
        if (cosmeticIds.isEmpty()) return null;

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
    public String deleteCosmeticToUser(User user, List<Long> cosmeticIdList) {
        List<Long> cosmeticList= user.getUserCosmeticList();

        if (cosmeticIdList.isEmpty()) {
            return "화장품 list가 없습니다.";
        }
        else {
            for (Object item : cosmeticIdList) {
                // 화장품 id가 유효하지 않은 경우
                if (!(item instanceof Long)) {
                    return item + "가 정수가 아닙니다.";
                }
                // 화장품 id가 존재하지 않는 경우: db에 존재하지 않음.
                if (!cosmeticList.contains(item)) {
                    return item + "가 존재하지 않습니다.";
                }
                // 중복된 id가 들어온 경우
                if (cosmeticIdList.indexOf(item) != cosmeticIdList.lastIndexOf(item)) {
                    return item + "가 중복되었습니다.";
                }
            }
            // 나의 화장대에 있는 화장품 id인지 확인 후 삭제
            if (cosmeticList.containsAll(cosmeticIdList)) {
                cosmeticList.removeAll(cosmeticIdList);
                user.setUserCosmeticList(cosmeticList);
                userRepository.updateUserCosmetic(user);  // 변경 내용을 db에 반영
                return "";
            } else {
                return "서버 에러";
            }
        }
    }
}
