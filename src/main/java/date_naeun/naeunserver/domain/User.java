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
    @Column(name = "user_id")
    private Long id;

    @Setter
    private String name;

    private String email;

    @Setter @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skinType_id")
    private SkinType skinType;

    @Setter
    private boolean Trouble;    // 여드름

    @Setter
    private boolean Whitening;  // 미백

    // 화장품의 id만 추가
    @ElementCollection @Setter
    @CollectionTable(name = "user_cosmetic_list", joinColumns = @JoinColumn(name = "user_id"))
    private List<Long> userCosmeticList = new ArrayList<>();


    // 선호성분의 id만 추가
    @ElementCollection @Setter
    @CollectionTable(name = "user_prefer_ingr_list", joinColumns = @JoinColumn(name = "user_id"))
    private List<Long> preferIngrList = new ArrayList<>();

    // 기피성분의 id만 추가
    @ElementCollection @Setter
    @CollectionTable(name = "user_dislike_ingr_list", joinColumns = @JoinColumn(name = "user_id"))
    private List<Long> dislikeIngrList = new ArrayList<>();

    // 사용자 권한
    @Enumerated(EnumType.STRING)
    private Role role;

    // 비교기록 id 리스트
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<History> historyList = new ArrayList<>();


    @Builder
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = Role.USER;
        this.Trouble = false;
        this.Whitening = false;
    }
}

