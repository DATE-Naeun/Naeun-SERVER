package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.Ingredient;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitIngredientDb {

    private final InitIngredientDbService initIngredientDbService;

    @PostConstruct
    public void init() { initIngredientDbService.initDb(); }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitIngredientDbService {

        private final EntityManager em;

        public void initDb() {
            try (FileInputStream fis = new FileInputStream("src/main/resources/Cosign_Dataset_v1.xlsx");
                 Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet ingrSheet = workbook.getSheetAt(2);
//                Sheet activeIngrSheet = workbook.getSheetAt(4);

                Iterator<Row> ingrRowIterator = ingrSheet.iterator();
                while (ingrRowIterator.hasNext()) {
                    Row row = ingrRowIterator.next();
                    if (row.getRowNum() == 0) { // 첫번째 행은 헤더
                        continue;
                    }

                    if (row.getCell(0) != null && !row.getCell(0).getStringCellValue().trim().isEmpty()) {/* 성분 이름 */
                        String ingrName = row.getCell(0).getStringCellValue().trim();
                        System.out.println(ingrName);

                        Ingredient ingredient = createIngr(ingrName, null, null);

                        em.persist(ingredient);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*static private Ingredient createIngr(String ingr_name) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngr_name(ingr_name);

        return ingredient;
    }*/

    static private Ingredient createIngr(String ingr_name,
                                         List<Long> active_detail_id,
                                         List<Long> harm_detail_id) {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngr_name(ingr_name);
        ingredient.setActive_detail_id(active_detail_id);
        ingredient.setHarm_detail_id(harm_detail_id);

        return ingredient;
    }
}
