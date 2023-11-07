package date_naeun.naeunserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 성분 엔티티
 */
@Getter
@Entity
@Table(name = "Ingredient")
@NoArgsConstructor
public class Ingredient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String ingr_name;

    @Setter
    @ElementCollection
    @CollectionTable(name = "ingr_active_detail", joinColumns = @JoinColumn(name = "active_id"))
    private List<Long> active_detail_id;

    @Setter
    @ElementCollection
    @CollectionTable(name = "ingr_harm_detail", joinColumns = @JoinColumn(name = "harm_id"))
    private List<Long> harm_detail_id;

    public Ingredient(String ingr_name) {
        this.ingr_name = ingr_name;
        this.active_detail_id = null;
        this.harm_detail_id = null;
    }

    //== detail 추가 로직 ==//
    public void addActiveDetail(Long detailId) {
        active_detail_id.add(detailId);
    }

    public void addHarmDetail(Long detailId) {
        harm_detail_id.add(detailId);
    }
}
