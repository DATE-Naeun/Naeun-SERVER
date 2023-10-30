package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.Ingredient;
import date_naeun.naeunserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class InitIngrDetailDb {

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitIngrDetailDbService {

        private final IngredientRepository ingredientRepository;

        public void initDb() {
            try (
                    FileInputStream fis = new FileInputStream("src/main/resources/Cosign_Dataset_v2.xlsx");
                    Workbook workbook = new XSSFWorkbook(fis)) {

                /* Init Ingr_Active_Detail */
                Sheet sheet1 = workbook.getSheetAt(3);
                System.out.println(sheet1.getSheetName());

                Iterator<Row> rowIterator1 = sheet1.iterator();
                while (rowIterator1.hasNext()) {
                    Row row = rowIterator1.next();
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    if (row.getCell(0) != null && row.getCell(1) != null) {
                        Long ingr_id = (long) row.getCell(0).getNumericCellValue();
                        Long detail_id = (long) row.getCell(1).getNumericCellValue();

                        Ingredient ingredient = ingredientRepository.find(ingr_id);

                        if (ingredient != null) {
                            if (ingredient.getActive_detail_id() == null) {
                                ingredient.setActive_detail_id(new ArrayList<>(Arrays.asList(detail_id)));
                            } else ingredient.addActiveDetail(detail_id);
                        }
                    }
                }

                /* Init Ingr_Harm_Detail */
                Sheet sheet2 = workbook.getSheetAt(5);
                System.out.println(sheet2.getSheetName());

                Iterator<Row> rowIterator2 = sheet2.iterator();
                while (rowIterator2.hasNext()) {
                    Row row = rowIterator2.next();
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    if (row.getCell(0) != null && row.getCell(1) != null) {
                        Long ingr_id = (long) row.getCell(0).getNumericCellValue();
                        Long detail_id = (long) row.getCell(1).getNumericCellValue();

                        Ingredient ingredient = ingredientRepository.find(ingr_id);

                        if (ingredient != null) {
                            if (ingredient.getHarm_detail_id() == null) {
                                ingredient.setHarm_detail_id(new ArrayList<>(Arrays.asList(detail_id)));
                            } else ingredient.addHarmDetail(detail_id);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
