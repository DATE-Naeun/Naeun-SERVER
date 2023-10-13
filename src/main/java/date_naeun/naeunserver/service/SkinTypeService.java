package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.dto.UpdateSkinTypeRequestDto;
import date_naeun.naeunserver.repository.SkinTypeRepository;
import date_naeun.naeunserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkinTypeService {

    private final SkinTypeRepository skinType_repository;
    private final UserRepository user_repository;

    /**
     * 피부 타입 등록
     */
    @Transactional
    public Long save(SkinType skinType) {
        validateDuplicateSkinType(skinType);
        skinType_repository.save(skinType);
        return skinType.getId();
    }

    private void validateDuplicateSkinType(SkinType skinType) {
        List<SkinType> findSkinType = skinType_repository.findOneSkinType(skinType.getTypeName());
        if (!findSkinType.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 피부타입입니다.");
        }
    }

    /**
     * 전체 피부 타입 조회
     */
    public List<SkinType> findAllSkinType() {
        return skinType_repository.findAll();
    }

    /**
     * 피부 타입 조회
     */
    public List<SkinType> findOneSkinType(String typeName) {
        return skinType_repository.findOneSkinType(typeName);
    }

    public SkinType findSkinType(String typeName) {
        return skinType_repository.findSkinType(typeName);
    }

    /**
     * user의 SkinType 등록
     */
    @Transactional
    public void update(Long userId, UpdateSkinTypeRequestDto updateSkinTypeRequestDto) {
        User user = user_repository.findOne(userId);
        user.setSkinType(findSkinType(updateSkinTypeRequestDto.getSkinType()));
        user.setTrouble(updateSkinTypeRequestDto.getTrouble());
        user.setWhitening(updateSkinTypeRequestDto.getWhitening());
    }

}
