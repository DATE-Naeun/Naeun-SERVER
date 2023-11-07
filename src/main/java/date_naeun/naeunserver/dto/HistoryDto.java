package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.domain.History;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 비교기록 조회를 위한 Dto
 */
@Data
public class HistoryDto {

    private Long comparisonId;
    private Date comparisonDate;
    private List<HistoryCosmeticDto> cosmeticList;

    public HistoryDto(History history, List<HistoryCosmeticDto> cosmeticDtos) {
        this.comparisonId = history.getId();
        this.comparisonDate = history.getDate();
        this.cosmeticList = cosmeticDtos;
    }

}
