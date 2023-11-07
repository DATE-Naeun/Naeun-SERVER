package date_naeun.naeunserver;

import date_naeun.naeunserver.domain.Cosmetic;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.apache.poi.ss.usermodel.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class InitCosmeticDb {

    private final InitCosmeticDbService initCosmeticDbService;
    private final InitIngredientDb.InitIngredientDbService initIngredientDbService;
    private final InitCsmtIngrDb.InitCsmtIngrDbService initCsmtIngrDbService;
    private final InitIngrDetailDb.InitIngrDetailDbService initIngrDetailDbService;

    @PostConstruct
    public void init() {
        initIngredientDbService.initDb();
        initCosmeticDbService.initDb();
        initCsmtIngrDbService.initDb();
        initIngrDetailDbService.initDb();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitCosmeticDbService {

        private final EntityManager em;

        public void initDb() {
            try (
                FileInputStream fis = new FileInputStream("src/main/resources/Cosign_Dataset_v2.xlsx");
                Workbook workbook = new XSSFWorkbook(fis)) {

                Sheet sheet = workbook.getSheetAt(1);

                Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (row.getRowNum() == 0) {
                        continue;
                    }

                    String name = row.getCell(0).getStringCellValue();
                    String brand = row.getCell(1).getStringCellValue();
                    String image = row.getCell(2).getStringCellValue();
                    String category = row.getCell(3).getStringCellValue();

                    Cell priceCell = row.getCell(4);
                    int priceInt = 0;

                    if (priceCell != null) {
                        if (priceCell.getCellType() == CellType.NUMERIC) {
                            double priceDouble = priceCell.getNumericCellValue();
                            priceInt = (int) priceDouble;
                        }
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("###,###");
                    String price = decimalFormat.format(priceInt);

                    Cosmetic cosmetic = Cosmetic.builder()
                            .name(name)
                            .brand(brand)
                            .image(image)
                            .category(category)
                            .price(price)
                            .build();

                    em.persist(cosmetic);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
