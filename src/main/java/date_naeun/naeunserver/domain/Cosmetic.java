package date_naeun.naeunserver.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 화장품 엔티티
 */
@Entity
@NoArgsConstructor
@Getter
@Table(name = "cosmetic")
public class Cosmetic {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String brand;

    @Column
    private String image;

    @Column
    private String category;

    @Column
    private Long price;

    @Column
    @ElementCollection @Setter
    @CollectionTable(name = "ingredient_list", joinColumns = @JoinColumn(name = "cosmetic_id"))
    private List<Long> ingredientList = new ArrayList<>();

    @Builder
    public Cosmetic(String name, String brand, String image, String category, Long price) {
        this.name = name;
        this.brand = brand;
        this.image = image;
        this.category = category;
        this.price = price;
    }
}
