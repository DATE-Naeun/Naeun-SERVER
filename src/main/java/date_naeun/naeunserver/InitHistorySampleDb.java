//package date_naeun.naeunserver;
//
//import date_naeun.naeunserver.domain.Cosmetic;
//import date_naeun.naeunserver.domain.History;
//import date_naeun.naeunserver.domain.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import javax.persistence.EntityManager;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@DependsOn("InitUser")
//@Slf4j
//public class InitHistorySampleDb {
//
//    private final InitHistoryService initHistoryService;
//
//
//    @PostConstruct
//    public void init() {
//        initHistoryService.dbInit();
//    }
//
//
//    @Component
//    @Transactional
//    @RequiredArgsConstructor
//    static class InitHistoryService {
//        private final EntityManager em;
//
//        public void dbInit() {
//            Date today = new Date();
//            List<Cosmetic> cosmetics = new ArrayList<>();
//            cosmetics.add()
//            log.info("cosmeticList = {}", cosmeticList);
//            History history = new History(today, cosmeticList);
//
//            log.info("history = {}", history);
//            em.persist(history);
//
//            User user = em.find(User.class, 1L);
//            log.info("user = {}", user.getName());
//
//            List<Long> historyList = new ArrayList<>();
//            historyList.add(history.getId());
//            log.info("historyList = {}", historyList);
//            user.setHistoryList(historyList);
//
//            em.persist(user);
//
//            log.info("update user historyList = {}", user.getHistoryList());
//        }
//    }
//}
