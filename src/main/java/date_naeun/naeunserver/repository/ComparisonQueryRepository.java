package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.Cosmetic;
import date_naeun.naeunserver.domain.User;
import date_naeun.naeunserver.exception.ApiErrorWithItemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static date_naeun.naeunserver.exception.ApiErrorStatus.*;

@Repository
@RequiredArgsConstructor
public class ComparisonQueryRepository{

    private final EntityManager em;
    private final CosmeticRepository cosmeticRepository;

    //== 화장품 비교 ==//
    private String createNativeQuery(Long[] idList, String skinType) {
        String cosmeticIds = Stream.of(idList)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return "SELECT csmt.* FROM Cosmetic as csmt JOIN" +
                " (SELECT ci.csmt_id as id, SUM(ci.weight * COALESCE(ingr_table.ingr_score, 0)) as csmt_score" +
                " FROM COSMETIC_INGREDIENT as CI" +
                " LEFT JOIN" +
                " (SELECT active_table.id as id, active_table.active_score - COALESCE(harm_table.harm_score, 0) as ingr_score" +
                " FROM" +
                " (SELECT id, ROUND(active_total / active_count * SQRT(active_count), 2) as active_score" +
                " FROM" +
                " (SELECT table1.active_id as id, " +
                "(COALESCE(total1, 0) + " +
                "(CASE WHEN total2 IS NULL OR total2 = 1.0 THEN 0 ELSE total2 END)) as active_total, " +
                "(COALESCE(count1, 0) + COALESCE(count2, 0)) as active_count" +
                " FROM" +
                " (SELECT IAD.ACTIVE_ID, COUNT(*) as count1, SUM(Weight * " + skinType + ") as total1" +
                " FROM INGR_ACTIVE_DETAIL AS IAD" +
                " JOIN ACTIVE_DETAIL AS AD ON IAD.ACTIVE_DETAIL_ID = AD.ID" +
                " WHERE AD.ID < 11" +
                " GROUP BY IAD.ACTIVE_ID) as table1" +
                " LEFT JOIN" +
                " (SELECT IAD.ACTIVE_ID, COUNT(*) as count2, SUM(Weight * " + skinType + ") as total2" +
                " FROM INGR_ACTIVE_DETAIL AS IAD" +
                " JOIN ACTIVE_DETAIL AS AD ON IAD.ACTIVE_DETAIL_ID = AD.ID" +
                " WHERE AD.ID >= 11" +
                " GROUP BY IAD.ACTIVE_ID) as table2" +
                " ON table1.ACTIVE_ID = table2.ACTIVE_ID)) as active_table" +
                " LEFT JOIN" +
                " (SELECT id, ROUND(harm_total / harm_count * SQRT(harm_count), 2) as harm_score" +
                " FROM" +
                " (SELECT table1.harm_id as id, " +
                "(COALESCE(total1, 0) + " +
                "(CASE WHEN total2 IS NULL OR total2 = 1.0 THEN 0 ELSE total2 END)) as harm_total, " +
                "(COALESCE(count1, 0) + COALESCE(count2, 0)) as harm_count" +
                " FROM" +
                " (SELECT IHD.harm_id, COUNT(*) as count1, SUM(Weight * " + skinType + ") as total1" +
                " FROM INGR_HARM_DETAIL AS IHD" +
                " JOIN HARMFUL_DETAIL AS HD ON IHD.HARM_DETAIL_ID = HD.ID" +
                " WHERE HD.ID < 11" +
                " GROUP BY IHD.HARM_ID) as table1" +
                " LEFT JOIN" +
                " (SELECT IHD.HARM_ID, COUNT(*) as count2, SUM(Weight * " + skinType + ") as total2" +
                " FROM INGR_HARM_DETAIL AS IHD" +
                " JOIN HARMFUL_DETAIL AS HD ON IHD.HARM_DETAIL_ID = HD.ID" +
                " WHERE HD.ID >= 11" +
                " GROUP BY IHD.harm_id) as table2" +
                " ON table1.HARM_ID = table2.HARM_ID)) as harm_table" +
                " ON active_table.id = harm_table.id) as ingr_table" +
                " ON ingr_table.id = CI.ingr_id" +
                " GROUP BY CI.csmt_id) as result_table" +
                " ON result_table.id = csmt.id" +
                " WHERE csmt.id IN (" + cosmeticIds + ") or (" + cosmeticIds + ") IS NULL" +
                " ORDER BY result_table.csmt_score DESC";
    }

    public List<Cosmetic> rankCosmetics(Long[] idList, User user) {
        Set<Long> uniqueIds = new HashSet<>();
        for (long id : idList) {
            Object obj = id;
            if (!(obj instanceof Long)) throw new ApiErrorWithItemException(NOT_INTEGER, id);
            if (!uniqueIds.add(id)) throw new ApiErrorWithItemException(DUPLICATED_ID, id);
            if (cosmeticRepository.findById(id) == null) throw new ApiErrorWithItemException(NOT_EXIST, id);
        }
        String skinType = createSkinTypeString(user);
        String nativeQuery = createNativeQuery(idList, skinType);
        Query query = em.createNativeQuery(nativeQuery, Cosmetic.class);
        return query.getResultList();
    }

    private String createSkinTypeString(User user) {
        String skinType = "";
        skinType += user.getSkinType().getTypeName().charAt(0);
        for (int i = 1; i < 4; i++) {
            skinType += " * ";
            skinType += user.getSkinType().getTypeName().charAt(i);
        }
        if (user.isTrouble()) skinType += " * Trouble";
        if (user.isWhitening()) skinType += " * Whitening";
        return skinType;
    }


    //== 특정 카테고리 idList 불러오기 ==//
    public Long[] getCosmeticsByCategory(int categoryId) {
        String category = "";
        switch (categoryId) {
            case 1:
                category = "크림";
                break;
            case 2:
                category = "선크림";
                break;
            case 3:
                category = "미백크림";
                break;
        }

        return em.createQuery("SELECT c.id FROM Cosmetic c " +
                "WHERE c.category = :category", Long.class)
                .setParameter("category", category)
                .getResultList().toArray(new Long[0]);
    }
}
