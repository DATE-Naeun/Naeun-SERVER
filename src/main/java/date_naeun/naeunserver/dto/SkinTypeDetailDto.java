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

    public SkinTypeDetailDto(Long id, String typeName, String detail, List<String> strong_point, List<String> weak_point, List<String> care) {
        this.id = id;
        this.typeName = typeName;
        this.detail = detail;
        this.strong_point = strong_point;
        this.weak_point = weak_point;
        this.care = care;
    }
}
