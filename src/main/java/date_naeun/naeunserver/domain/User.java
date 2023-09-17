package date_naeun.naeunserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String name;

    private String email;

//    private List<Cosmetic> cosmeticList;
//    private List<Ingredient> preferIngrList;
//    private List<Ingredient> dislikeIngrList;
//    private String skinType;


    @Builder
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

