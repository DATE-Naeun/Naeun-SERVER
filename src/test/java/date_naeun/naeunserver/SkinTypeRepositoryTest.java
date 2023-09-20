package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.repository.SkinTypeRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SkinTypeRepositoryTest {
    @Autowired
    SkinTypeRepository skinTypeRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testSkinType(){
        SkinType skinType = new SkinType();
        skinType.setTypeName("OSPT");
        Long savedId = skinTypeRepository.save(skinType);
        SkinType findSkinType = skinTypeRepository.find(savedId);
        Assertions.assertThat(findSkinType.getId()).isEqualTo(skinType.getId());
    }
}
