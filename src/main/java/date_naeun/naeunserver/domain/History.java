package date_naeun.naeunserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "skin_type_id")
    private SkinType skinType;

    // 비교 날짜
    private Date date;

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL)
    private final List<HistoryCosmetic> historyCosmeticList = new ArrayList<>();

    public History(User user, List<Cosmetic> cosmeticList) {
        this.user = user;
        this.date = new Date();
        this.skinType = user.getSkinType();

        for (Cosmetic cosmetic: cosmeticList) {
            HistoryCosmetic historyCosmetic = new HistoryCosmetic(this, cosmetic);
            this.historyCosmeticList.add(historyCosmetic);
        }
    }
}
