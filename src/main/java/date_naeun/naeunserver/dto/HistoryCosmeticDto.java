package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.Cosmetic;
import lombok.Data;

/**
 * 비교기록 화장품 Dto
 */
@Data
public class HistoryCosmeticDto {
    private Long cosmeticId;
    private String cosmeticName;
    private String cosmeticBrand;
    private String image;

    public HistoryCosmeticDto(Cosmetic cosmetic) {
        this.cosmeticId = cosmetic.getId();
        this.cosmeticName = cosmetic.getName();
        this.cosmeticBrand = cosmetic.getBrand();
        this.image = cosmetic.getImage();
    }
}
