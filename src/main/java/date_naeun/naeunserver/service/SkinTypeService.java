package date_naeun.naeunserver.service;

import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.repository.SkinTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SkinTypeService {

    private final SkinTypeRepository skinType_repository;

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
    public List<SkinType> findALlSkinType() {
        return skinType_repository.findAll();
    }

    /**
     * 피부 타입 조회
     */
    public List<SkinType> findOneSkinType(String typeName) {
        return skinType_repository.findOneSkinType(typeName);
    }

}
