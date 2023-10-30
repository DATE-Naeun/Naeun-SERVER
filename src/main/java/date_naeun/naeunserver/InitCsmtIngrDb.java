package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.CosmeticIngredient;
import date_naeun.naeunserver.repository.CosmeticRepository;
import date_naeun.naeunserver.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class InitCsmtIngrDb {

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitCsmtIngrDbService {

        private final EntityManager em;
        private final CosmeticRepository cosmeticRepository;
        private final IngredientRepository ingredientRepository;

        public void initDb() {
            try (
                FileInputStream fis = new FileInputStream("src/main/resources/Cosign_Dataset_v2.xlsx");
                Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null) {
                        Long csmt_id = (long) row.getCell(0).getNumericCellValue();
                        Long ingr_id = (long) row.getCell(1).getNumericCellValue();
                        Double weight = row.getCell(2).getNumericCellValue();

                        createCosmetic(csmt_id, ingr_id, weight);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void createCosmetic(Long csmt_id,
                                    Long ingr_id, Double weight) {

            CosmeticIngredient cosmeticIngredient;
            cosmeticIngredient = new CosmeticIngredient(cosmeticRepository.findById(csmt_id), ingredientRepository.find(ingr_id), weight);
            em.persist(cosmeticIngredient);
        }
    }
}
