package date_naeun.naeunserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 성분 엔티티
 */
@Getter
@Entity
@Table(name = "Ingredient")
public class Ingredient {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Setter
    private String ingr_name;

    @Setter
    @ElementCollection
    @CollectionTable(name = "ingr_active_detail", joinColumns = @JoinColumn(name = "ingr_id"))
    private List<String> active_detail;

    @Setter
    @ElementCollection
    @CollectionTable(name = "ingr_harm_detail", joinColumns = @JoinColumn(name = "ingr_id"))
    private List<String> harm_detail;

}
