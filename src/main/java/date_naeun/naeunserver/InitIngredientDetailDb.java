package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.ActiveDetail;
import date_naeun.naeunserver.domain.HarmfulDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitIngredientDetailDb {

    private final InitIngredientDetailService initIngredientDetailService;

    @PostConstruct
    public void init() {
        initIngredientDetailService.dbInitActive();
        initIngredientDetailService.dbInitHarmful();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitIngredientDetailService {

        private final EntityManager em;

        public void dbInitActive() {
            /* 기본 점수 */
            createIngredientActiveDetail("피부 손상 방지", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientActiveDetail("피부 유연화", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientActiveDetail("피부 면역력 강화", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientActiveDetail("노폐물 감소", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientActiveDetail("자외선 차단", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);

            /* 기본 점수 가중치 */
            createIngredientActiveDetail("보습", 1.2, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientActiveDetail("보습-D", 1.2, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientActiveDetail("보습-O", 1.2, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);

            /* 기본 점수 + 특정 피부타입 가중치 */
            createIngredientActiveDetail("각질 감소", 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("피지 분비 감소", 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);

            /* 특정 피부타입 가중치 */
            createIngredientActiveDetail("자극 완화", 1.0, 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("항염 효과", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("트러블 진정", 1.0, 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("피부 진정", 1.0, 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("피부 진정-D", 1.0, 1.0, 0.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("피부 진정-O", 1.0, 0.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("항산화 효과(노화 방지)", 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5, 1.0, 1.0);
            createIngredientActiveDetail("피부 재생", 1.0, 1.0, 1.0, 1.5, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("피부 재생-D", 1.0, 1.0, 0.0, 1.5, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientActiveDetail("피부 재성-O", 1.0, 0.0, 1.0, 1.5, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.5);

            /* 특정 피부타입 가중치 - 미백 */
            createIngredientActiveDetail("미백(식약처 인증)", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.8, 1.0);
            createIngredientActiveDetail("피부톤 개선", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5, 1.0);
            createIngredientActiveDetail("피부톤 개선-D", 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5, 1.0);
            createIngredientActiveDetail("피부톤 개선-O", 1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5, 1.0);


            /* 특정 피부타입 가중치 - W */
            createIngredientActiveDetail("주름 개선(식약처 인증)", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.8, 1.0, 1.0);
            createIngredientActiveDetail("콜라겐 합성 촉진", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.65, 1.0, 1.0);
            createIngredientActiveDetail("피부 탄력 개선", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5, 1.0, 1.0);
        }

        public void dbInitHarmful() {
            /* 기본 점수 */
            createIngredientHarmDetail("각질 증가", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientHarmDetail("피부 노화 촉진", 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);

            /* 기본 점수 가중치 */
            createIngredientHarmDetail("축적 시 독성물질", 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);

            /* 기본 점수 + 특정 피부타입 가중치 */
            createIngredientHarmDetail("모공 막힘", 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);

            /* 특정 피부타입 가중치 */
            createIngredientHarmDetail("알레르기 유발", 1.0, 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientHarmDetail("트러블 유발", 1.0, 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientHarmDetail("피부 건조증 유발", 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
            createIngredientHarmDetail("피부 자극", 1.0, 1.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientHarmDetail("피부 자극-D", 1.0, 1.0, 0.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
            createIngredientHarmDetail("피부 자극-O", 1.0, 0.0, 1.0, 1.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.5);
        }

        private void createIngredientActiveDetail(String description, Double weight, Double d, Double o, Double s, Double r, Double p, Double n, Double t, Double w, Double whitening, Double trouble) {
            ActiveDetail activeDetail
                    = new ActiveDetail(description, weight, d, o, s, r, p, n, t, w, whitening, trouble);

            em.persist(activeDetail);
        }

        private void createIngredientHarmDetail(String description, Double weight, Double d, Double o, Double s, Double r, Double p, Double n, Double t, Double w, Double whitening, Double trouble) {
            HarmfulDetail harmfulDetail
                    = new HarmfulDetail(description, weight, d, o, s, r, p, n, t, w, whitening, trouble);

            em.persist(harmfulDetail);
        }
    }
}
