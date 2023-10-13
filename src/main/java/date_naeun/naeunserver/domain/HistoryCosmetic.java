package date_naeun.naeunserver.domain;


import javax.persistence.*;

@Entity
@Table(name = "HistoryCosmetic")
public class HistoryCosmetic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_cosmetic_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "history_id")
    private final History history;

    @ManyToOne
    @JoinColumn(name = "cosmetic_id")
    private final Cosmetic cosmetic;

    public HistoryCosmetic(History history, Cosmetic cosmetic) {
        this.history = history;
        this.cosmetic = cosmetic;
    }
}
