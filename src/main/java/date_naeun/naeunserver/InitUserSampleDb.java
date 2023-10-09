package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component("InitUser")
@RequiredArgsConstructor
public class InitUserSampleDb {

    private final InitUserService initUserService;

    @PostConstruct
    public void init() {
        initUserService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitUserService {
        private final EntityManager em;

        public void dbInit() {
            User user = User.builder()
                    .name("minsu")
                    .email("minsu@naver.com").build();

            em.persist(user);
        }
    }
}
