package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.Cosmetic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitCosmeticSampleDb {

    private final InitCosmeticService initCosmeticService;

    @PostConstruct
    public void init() {
        initCosmeticService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitCosmeticService {
        private final EntityManager em;

        public void dbInit() {
            Cosmetic cosmetic1 = Cosmetic.builder()
                    .name("리얼 시카 카밍 95 크림")
                    .brand("웰라쥬")
                    .image("https://m.wellage.co.kr/web/product/extra/big/202109/3180f25ee5a054775a127372342b852e.jpg")
                    .price(21000L)
                    .build();

            List<Long> ingreList = new ArrayList<>();
            ingreList.add(1L);
            ingreList.add(2L);

            cosmetic1.setIngredientList(ingreList);

            em.persist(cosmetic1);
        }
    }
}
