package date_naeun.naeunserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원 엔티티
 */
@Getter
@Entity
@Table(name="user_table")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Setter
    private String name;

    private String email;

    @Setter @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skinType_id")
    private SkinType skinType;

    // 화장품의 id만 추가
    @ElementCollection
    @CollectionTable(name = "user_cosmetic_list", joinColumns = @JoinColumn(name = "user_id"))
    private final List<Long> userCosmeticList = new ArrayList<>();

//    private List<Ingredient> preferIngrList;
//    private List<Ingredient> dislikeIngrList;


    @Builder
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

