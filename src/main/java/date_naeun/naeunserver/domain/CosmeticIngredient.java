package date_naeun.naeunserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class CosmeticIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "csmt_id")
    private Cosmetic cosmetic;

    @ManyToOne
    @JoinColumn(name = "ingr_id")
    private Ingredient ingredient;

    private Double weight;

    public CosmeticIngredient(Cosmetic cosmetic, Ingredient ingredient, Double weight) {
        this.cosmetic = cosmetic;
        this.ingredient = ingredient;
        this.weight = weight;
    }
}
