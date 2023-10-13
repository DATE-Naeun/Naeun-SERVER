package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.SkinType;
import lombok.Data;

import java.util.List;

@Data
public class SkinTypeDetailDto {

    private String skinType;
    private String detail;
    private List<String> strongPoint;
    private List<String> weakPoint;
    private List<String> care;

    public SkinTypeDetailDto(SkinType skinType) {
        this.skinType = skinType.getTypeName();
        detail = skinType.getDetail();
        strongPoint = skinType.getStrongPoint();
        weakPoint = skinType.getWeakPoint();
        care = skinType.getCare();
    }
}
