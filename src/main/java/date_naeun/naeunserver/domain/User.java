package date_naeun.naeunserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 회원 엔티티
 */
@Getter
@Entity
@Table(name="User_table")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    private String name;

    private String email;

    @Setter
    private String skinType;

//    private List<Cosmetic> cosmeticList;
//    private List<Ingredient> preferIngrList;
//    private List<Ingredient> dislikeIngrList;


    @Builder
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

