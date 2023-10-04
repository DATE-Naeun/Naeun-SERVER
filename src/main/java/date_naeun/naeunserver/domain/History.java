package date_naeun.naeunserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "History")
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 비교 날짜
    private Date date;

    // 비교한 화장품 리스트
    @ElementCollection
    @CollectionTable(name = "comparison_cosmetic_list", joinColumns = @JoinColumn(name = "cosmetic_id"))
    private List<Long> cosmeticList;

    public History(Date date, List<Long> cosmeticList) {
        this.date = date;
        this.cosmeticList = cosmeticList;
    }
}
