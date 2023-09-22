package date_naeun.naeunserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

//    @Column
//    private List<Ingredient> ingredientList;


    public Cosmetic(Long id, String name, String brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }
}
