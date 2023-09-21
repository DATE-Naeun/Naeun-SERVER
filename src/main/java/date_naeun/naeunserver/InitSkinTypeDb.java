package date_naeun.naeunserver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import date_naeun.naeunserver.domain.SkinType;
import date_naeun.naeunserver.repository.SkinTypeRepository;
import date_naeun.naeunserver.service.SkinTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitSkinTypeDb {

    private final InitSkinTypeService initSkinType_service;

    @PostConstruct
    public void init() {
        initSkinType_service.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitSkinTypeService {
        private final EntityManager em;

        public void dbInit() {
            SkinType dspt = createSkinType("DSPT", "피부 민감성 때문에 새로운 제품을 사용해 볼 자신이 없을 정도로 많은 성분에 예민하게 반응하는 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("특별한 장점이 없습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "습진, 기미, 건조함, 여드름, 피부 벗겨짐, 붉어짐, 가려움, 피부염의 증상을 가지기 쉽고, 상처가 아무는 데 오래 걸리는 피부 타입입니다.",
                            "스킨 케어 제품의 향, 비누의 세제, 거친 직물, 바람 많이 부는 날씨는 피부에 트러블을 일으킵니다.",
                            "노화와 환경조건, 화학적 노출로 인해 피부가 더 건조하고 민감해 질 수 있습니다."
                            )),
                    new ArrayList<>(Arrays.asList(
                            "일단 피부에 염증이 생기면, 자극제를 피하고 진정제를 발라 피부를 진정시켜야 합니다.",
                            "각질 제거를 추천하지 않습니다.",
                            "적어도 하루에 두 번은 보습제를 발라야하며, 많을수록 좋습니다.",
                            "자외선 차단제를 매일 사용해야 합니다.",
                            "거품이 많이 나는 클렌저는 건조함과 민감도를 높이기 때문에 되도록 사용하지 않는 것이 좋습니다."))
            );
            em.persist(dspt);

            SkinType dsnt = createSkinType("DSNT","피부가 예민해 알레르기로 가장 고통 받는 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("특별한 장점이 없습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "지금 주름이 없어도 관리가 필요합니다.",
                            "건조함, 박리, 가려움증, 부스럼, 붉어짐 현상이 잘 일어납니다.",
                            "피부가 예민해 스킨케어 성분, 비누 및 클렌저, 금이나 백금이 아닌 다른 금속에 염증이 발생할 수 있습니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "피부의 천연 지질을 벗겨내는 성분(알코올)을 포함하거나, 거품이 많이 나는 클렌저를 피해야 합니다.",
                            "보습제를 사용해 가능한 많은 수분 공급을 해줘야합니다.",
                            "아침에는 저녁보다 가벼운 제형의 보습제를 추천합니다.",
                            "일주일에 한 번 부드러운 제형의 스크럽으로 각질 제거를 해주면 좋습니다."
                    ))
            );
            em.persist(dsnt);

            SkinType dspw = createSkinType("DSPW", "가장 고통스러운 피부타입 중 하나로, 관리하기 어려운 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("특별한 장점이 없습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "피부가 얇고, 건조하고 민감합니다.",
                            "피부가 예민하여 천연 성분까지도 자극적으로 느껴질 수 있습니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "건조하면 무거운 크림 제품을, 습도가 높으면 가벼운 제품으로 변경하며 계절에 따라 피부 관리 방법을 바꿔야 합니다.",
                            "클렌징 크림이나 클렌징 오일 등 건조함이 덜한 클렌저를 사용하는 것이 좋습니다.",
                            "세안 후 얼굴에 수분이 남아 있을 때 보습제를 발라야 합니다.",
                            "거품이 나는 클렌저를 피해야 합니다."
                    ))
            );
            em.persist(dspw);

            SkinType dsnw = createSkinType("DSNW", "고통스러운 피부타입 중 하나이며, 예측할 수 없는 피부로 피부의 컨디션이 늘 변화무쌍한 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("특별한 장점이 없습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "민감한 피부라 민감한 피부를 위해 설계된 제품도 자극적으로 느껴질 수 있습니다.",
                            "건조한 날씨에 최악인 피부이며, 건조하면 주름이 더 심해집니다.",
                            "태양 노출에 가장 취약한 피부입니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "적어도 하루에 두 번은 수분공급을 해줘야하고, 겨울이나 습도가 낮으면 더 자주 발라야 합니다.",
                            "부드럽고 촉촉한 클렌저를 화장솜이나 손에 소량 덜어 얼굴 전체에 부드럽게 원을 그리며 닦아내는 것을 추천합니다.",
                            "항염증 성분이 함유된 세럼을 사용하는 것을 추천합니다.",
                            "마스크팩이 굉장히 도움됩니다."
                    ))
            );
            em.persist(dsnw);

            SkinType ospt = createSkinType("OSPT", "지성과 민감성이 겹쳐 잡티의 악순환에 빠지기 쉬운 피부타입으로 예방이 가장 중요한 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("다른 피부 타입보다 노화에 강한 편으로 피부 관리를 꾸준히 했다면 50~60대의 나이에서 피부가 빛을 발합니다.")),
                    new ArrayList<>(Arrays.asList(
                            "피부의 기름기로 자외선 차단제를 바르면 얼굴이 번들거리고, 자극적이게 느껴집니다.",
                            "피부의 기름기가 여드름으로 이어지기 쉽습니다.",
                            "피부 민감도가 높아 화학 제모, 털 뽑기 등에 피부가 상할 수 있습니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "자극적인 클렌저, 클렌징 크림은 피해야 합니다.",
                            "보습제를 지나치게 쓰면 모공을 막고 유분을 증가시킬 수 있습니다. 눈, 볼, 턱 주변에 건조한 부분에만 선택적으로 바르는 것을 추천합니다.",
                            "묽은 선크림보단 선스틱, 선스프레이 등을 추천합니다."
                    ))
            );
            em.persist(ospt);

            SkinType osnt = createSkinType("OSNT", "지성이고 민감하며, 안면 홍조가 가장 고민인 피부타입입니다.",
                    new ArrayList<>(Arrays.asList(
                            "유분은 나이가 들수록 개선될 것입니다.",
                            "노화에 영향을 잘 받지 않는 피부타입입니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "안면 홍조가 있고, 얼굴에 혈관이 잘 보입니다.",
                            "민감한 피부, 그리고 기름진 피부라 제품들이 자극적이고 기름지게 느껴집니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "클렌저는 피부 표면의 유분을 줄여주는 소염성분이 함유된 제품을 사용하는 것이 좋습니다.",
                            "열을 떨어뜨리는 제품을 사용하는 것을 추천합니다.",
                            "마스크팩은 매일 해도 괜찮습니다.",
                            "보습제를 사용하는 것은 추천하지 않습니다."
                    ))
            );
            em.persist(osnt);

            SkinType ospw = createSkinType("OSPW", "햇빛에 잘 타는 피부타입이며, 이 피부타입에서 주름은 유전의 영향보다는 스스로의 행동이나 생활습관에 의한 것일 가능성이 높습니다.",
                    new ArrayList<>(Arrays.asList("태닝이 잘 됩니다.")),
                    new ArrayList<>(Arrays.asList(
                            "피부의 기름기로 자외선 차단제를 바르면 얼굴이 번들거리고, 자극적이게 느껴집니다.",
                            "잘 타는 피부이기에 노화에 취약해, 태양에 노출되면 주름과 갈색 반점 등의 많은 피부 문제가 생깁니다. 특히 주름은 빠르면 20대 후반부터 생길 수 있습니다.",
                            "기름기 있는 피부라서 여드름이 자주 발생하고 여드름 흉터가 잘 생깁니다.",
                            "피부가 예민하여 스킨케어 제품들을 쓸 때 따끔거리거나 화끈거립니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "피부 보호를 위해 노화에 효과적인 항산화제를 섭취해야 합니다.",
                            "햇빛에 의한 피부손상이 취약하니 자외선 차단제를 꼭 발라야합니다. 민감한 피부이기 때문에 차단제 성분을 잘 골라야 합니다.",
                            "민감한 피부이니 주름과 어두운 반점 치료는 매일 항산화, 항염증 성분 제품을 쓰며 최대한 조심스럽게 해야 합니다."
                    ))
            );
            em.persist(ospw);

            SkinType osnw = createSkinType("OSNW", "피부톤 자체는 고르나 멜라닌 색소가 부족하기 때문에 햇빛에 피부가 빨개지는 타입으로 태닝이 힘들고, 홍조와 로사시아에 취약한 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("특별한 장점이 없습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "코 주변에 발진이 잘 나며, 피부혈관이 비쳐보이고 얼굴에 홍조가 있습니다.",
                            "젊을 때도 여드름에 고생하지만 다른 타입과 달리 나이가 들어도 여드름으로 고통받을 가능성이 있습니다. 그래도 폐경으로 인해 기름기는 감소합니다.",
                            "주름이 쉽게 지는 피부지만, 예민하기에 피부관리제품 사용 시 여드름, 화상, 홍조, 따끔거림이 생길 가능성이 커, 안티에이징 제품을 함부로 시도하면 안 됩니다.",
                            "피부의 기름기로 자외선 차단제를 바르면 얼굴이 번들거리고, 자극적이게 느껴집니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "여드름과 홍조를 제한하는 항염증성 제품을 사용하는 것이 좋습니다.",
                            "항산화 식품과 성분이 많은 도움이 됩니다.",
                            "기름지게 느껴지지 않는 자외선 차단제를 찾아 꼭 사용해야 합니다."
                    ))
            );
            em.persist(osnw);

            SkinType orpt = createSkinType("ORPT", "젊은 시절에 여드름과 어두운 반점이 생길 수 있지만, 나이가 들면 피부 트러블은 줄어드는 피부타입입니다. 다른 피부타입에 비해 관리가 쉬운 피부타입입니다.",
                    new ArrayList<>(Arrays.asList(
                            "민감성피부와 다르게 피부가 따끔거림, 붉어짐, 제품 알러지가 발생하지 않습니다.",
                            "다른 피부타입보다 노화에 강한 편입니다.",
                            "이미 탄력 있는 피부 유전자를 가졌기 때문에 이를 유지할 생활 습관만 가지면 됩니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "피부의 기름기로 자외선 차단제를 바르면 얼굴이 번들거립니다.",
                            "피부색이 밝으면, 햇빛에 의해 반점이 생길 수 있습니다.",
                            "특히 ORTP 남자에게 인그로운 헤어는 어두운 반점을 생성할 수 있습니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "피부 탄력을 유지하기 위해 항산화 성분이 풍부한 과일과 야채를 많이 먹고, 담배를 피우지 않고, 자외선 차단제를 바르고, 햇빛을 쬐면 안 됩니다.",
                            "얼굴에 기름기를 조절하기 위해 얼굴에 닿을 수 있는 것들을 주의해야 합니다.",
                            "기름지지 않고 갈색 반점을 유발하지 않는 항염제품을 찾아야 합니다."
                    ))
            );
            em.persist(orpt);

            SkinType ornt = createSkinType("ORNT", "여드름도, 홍조도, 어두운 반점도 없는 가장 완벽한 피부타입입니다. 심지어 나이가 들면 피부의 기름기 또한 정상화됩니다.",
                    new ArrayList<>(Arrays.asList(
                            "젊을 때 피부는 기름진 경향에 속하지만, 40대가 되면 피부의 기름기는 정상화됩니다.",
                            "이미 탄력 있는 피부 유전자를 가졌기 때문에 이를 유지할 생활 습관만 가지면 됩니다.",
                            "저항력이 강한 피부를 가졌기 때문에 다양한 스킨케어제품을 시도해도 됩니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "피부의 기름기로 자외선 차단제를 바르면 얼굴이 번들거립니다.",
                            "스킨케어제품은 피부의 기름 생산을 영구적으로 완벽히 감소시킬 수 없기 때문에 기름기의 지속적인 관리가 필수적입니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "피부 탄력을 유지하기 위해 항산화 성분이 풍부한 과일과 야채를 많이 먹고, 담배를 피우지 말고, 자외선 차단제를 바르고, 햇빛을 쬐지 말아야 합니다.",
                            "블랙헤드가 많으면 레티노이드 처방을 받아야 합니다."
                    ))
            );
            em.persist(ornt);

            SkinType orpw = createSkinType("ORPW", "주름과 어두운 반점들이 고민인 피부타입이고, 저항성 피부이기에 유용한 성분도 잘 흡수되지 않지만 여러 제품을 부정적인 반응 없이 사용해 볼 수 있는 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("주름은 흡연과 일광욕을 끊으면 피부관리제품으로 좋아질 수 있습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "자외선 차단제를 바르면 얼굴이 번들거립니다.",
                            "얼굴 기름짐, 모공 확대, 어두운 반점들은 빠른 시일 내에 개선될 수 없습니다.",
                            "나이가 들어도 피부 고민이 사라지지 않습니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "주름과 어두운 반점을 예방하기 위해 자외선 차단제, 레티노이드, 레티놀 사용을 권장합니다.",
                            "주름을 예방하기 위해 금연, 햇빛 피하기, 항산화성분이 풍부한 과일과 야채를 많이 먹는 생활습관이 필요합니다.",
                            "시중 화장품엔 혹시 모를 부작용을 위해 강한 성분을 넣지 않기 때문에, 피부과를 찾아가 처방을 받는 것을 추천합니다."
                    ))
            );
            em.persist(orpw);

            SkinType ornw = createSkinType("ORNW", "대부분의 스킨 케어 제품이 잘 맞지만 피부 노화로 인해 생기는 문제들이 나이를 먹으면 나타나는 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("여드름이 많이 나지 않고 관리가 쉽습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "어릴 때부터 주름 예방이 필요합니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "항산화 성분을 섭취하거나 포함된 제품을 사용해야 합니다.",
                            "밖에 나가지 않을 때도 자외선 차단제를 꾸준히 발라야 합니다.",
                            "아침에 크림이나 로션 형태의 보습제보다 묽은 제형의 세럼을 바르는 것이 좋습니다."
                    ))
            );
            em.persist(ornw);

            SkinType drpt = createSkinType("DRPT", "약간 건조할 수 있지만 피부 노화로 인한 문제가 거의 나타나지 않는 피부타입입니다.",
                    new ArrayList<>(Arrays.asList(
                            "주름이 잘 생기기 않습니다.",
                            "관리하기 쉽습니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "다크서클이 잘 생깁니다.",
                            "주근깨, 기미 등이 잘 생깁니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "자외선 차단 기능이 있는 보습제를 바르면 좋습니다.",
                            "알코올 성분은 건조하게 느껴지기 때문에 처음 7개 성분에 알코올이 있는 제품은 피하는 것이 좋습니다.",
                            "글라이콜릭애씨드가 포함된 클렌저를 쓰면 좋습니다."
                    ))
            );
            em.persist(drpt);

            SkinType drnt = createSkinType("DRNT", "축복받은 피부로, 건강한 생활습관을 유지하면 관리도 수월한 피부타입입니다.",
                    new ArrayList<>(Arrays.asList(
                            "눈에 띄는 피부 문제가 거의 없습니다.",
                            "대부분의 스킨 케어 제품이 잘 맞습니다.",
                            "피부톤이 전체적으로 고릅니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "물에 오래 있는 활동을 하면 피부가 쉽게 건조해집니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "수분 흡수를 도와주는 보습제와 수분 증발을 차단하는 보습제를 같이 발라주면 좋습니다.",
                            "보습만 잘 해주면 됩니다."
                    ))
            );
            em.persist(drnt);

            SkinType drpw = createSkinType("DRPW", "젊을 때는 좋은 피부이지만, 나이가 들면서 피부 노화가 가속화되는 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("20대까지는 여드름도 잘 나지 않고, 화장품 때문에 얼굴이 뒤집어지지 않습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "건조한 환경에서 피부가 거칠어집니다.",
                            "30대 때부터 피부 노화가 나타납니다.",
                            "멍이 잘 듭니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "보습이 중요합니다.",
                            "자외선 차단제를 꾸준히 발라야 합니다.",
                            "글라이콜릭애씨드나 항산화 성분을 포함한 클렌저를 사용하면 좋습니다."
                    ))
            );
            em.persist(drpw);

            SkinType drnw = createSkinType("DRNW", "젊을 때는 피부 관리하기 쉽지만, 30대부터 피부 노화가 나타나기 시작하는 피부타입입니다.",
                    new ArrayList<>(Arrays.asList("화장품 때문에 생기는 문제가 거의 없습니다.")),
                    new ArrayList<>(Arrays.asList(
                            "30대부터 주름이 생기기 시작합니다."
                    )),
                    new ArrayList<>(Arrays.asList(
                            "어릴 때부터 자외선 차단제를 매일 바르고, 주름 예방 관리를 해주면 좋습니다.",
                            "보습이 중요하기 때문에 물을 자주 마셔야 합니다.",
                            "피부가 건조해지기 때문에 비누 대신 약산성 클렌저로 세안하면 좋습니다."
                    ))
            );
            em.persist(drnw);
        }
    }



        /*@PostConstruct
        @Transactional
        public void initializeSkinTypes() throws IOException {
            ClassPathResource resource = new ClassPathResource("skinType.json");
            ObjectMapper object_mapper = new ObjectMapper();

            List<SkinType> skinTypes = object_mapper.readValue(resource.getInputStream(),
                    new TypeReference<List<SkinType>>() {});

            for (SkinType skinType : skinTypes) {
                skinType_repository.save(skinType);
            }
        }*/

        /*private final EntityManager em;
        SkinType dspt = createSkinType();*/

        static private SkinType createSkinType(String typeName,
                                        String detail,
                                        List<String> strong_point,
                                        List<String> weak_point,
                                        List<String> care) {
            SkinType skinType = new SkinType();
            skinType.setTypeName(typeName);
            skinType.setDetail(detail);
            skinType.setStrongPoint(strong_point);
            skinType.setWeakPoint(weak_point);
            skinType.setCare(care);

            return skinType;
        }

}
