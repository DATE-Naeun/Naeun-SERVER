package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.SkinType;
import lombok.Data;

import java.util.List;

@Data
public class SkinTypeDetailDto {

    private Long id;
    private String typeName;
    private String detail;
    private List<String> strong_point;
    private List<String> weak_point;
    private List<String> care;

    public SkinTypeDetailDto(SkinType skinType) {
        id = skinType.getId();
        typeName = skinType.getTypeName();
        detail = skinType.getDetail();
        strong_point = skinType.getStrongPoint();
        weak_point = skinType.getWeakPoint();
        care = skinType.getCare();
    }
}
