package date_naeun.naeunserver.repository;

import date_naeun.naeunserver.domain.Cosmetic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComparisonQueryRepository extends JpaRepository<Cosmetic, Long> {

    //== 화장품 비교 ==//
    @Query(value = "SELECT csmt.* FROM Cosmetic as csmt JOIN" +
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
            " (SELECT IAD.ACTIVE_ID, COUNT(*) as count1, SUM(WEIGHT * D * R * N * W) as total1" +
            " FROM INGR_ACTIVE_DETAIL AS IAD" +
            " JOIN ACTIVE_DETAIL AS AD ON IAD.ACTIVE_DETAIL_ID = AD.ID" +
            " WHERE AD.ID < 11" +
            " GROUP BY IAD.ACTIVE_ID) as table1" +
            " LEFT JOIN" +
            " (SELECT IAD.ACTIVE_ID, COUNT(*) as count2, SUM(WEIGHT * D * R * N * W) as total2" +
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
            " (SELECT IHD.harm_id, COUNT(*) as count1, SUM(WEIGHT * D * R * N * W) as total1" +
            " FROM INGR_HARM_DETAIL AS IHD" +
            " JOIN HARMFUL_DETAIL AS HD ON IHD.HARM_DETAIL_ID = HD.ID" +
            " WHERE HD.ID < 11" +
            " GROUP BY IHD.HARM_ID) as table1" +
            " LEFT JOIN" +
            " (SELECT IHD.HARM_ID, COUNT(*) as count2, SUM(WEIGHT * D * R * N * W) as total2" +
            " FROM INGR_HARM_DETAIL AS IHD" +
            " JOIN HARMFUL_DETAIL AS HD ON IHD.HARM_DETAIL_ID = HD.ID" +
            " WHERE HD.ID >= 11" +
            " GROUP BY IHD.harm_id) as table2" +
            " ON table1.HARM_ID = table2.HARM_ID)) as harm_table" +
            " ON active_table.id = harm_table.id) as ingr_table" +
            " ON ingr_table.id = CI.ingr_id" +
            " GROUP BY CI.csmt_id) as result_table" +
            " ON result_table.id = csmt.id" +
            " WHERE csmt.id IN :idList or :idList IS NULL" +
            " ORDER BY result_table.csmt_score DESC", nativeQuery = true)
    List<Cosmetic> rankCosmetics(@Param("idList") Long[] idList);


    /**
     * 완료
     * - sql문으로 결과 도출
     * - 비교하고 싶은 id만 비교 가능
     * - 전체 화장품도 가져올 수 있도록 수정
     * - comparison dto
     *
     * TO DO
     * - 피부타입 파라미터 넣기
     * - 카테고리 파라미터 넣기
     */
}
