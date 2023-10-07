package date_naeun.naeunserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "review")
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Setter
    private double rating;
    @Setter
    private boolean isOpen;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date")
    @Setter
    private Date date;

    @PrePersist
    protected void onCreate(){
        date = new Date();
    }

    @Setter
    private String content;
    @Setter
    private String texture;
    @Setter
    private String repurchase;

    @ElementCollection @Setter
    @CollectionTable(name = "review_image_list", joinColumns = @JoinColumn(name = "review_id"))
    private List<String> image;

    @ManyToOne(fetch = FetchType.LAZY) @Setter
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @Setter
    @JoinColumn(name = "csmt_id")
    private Cosmetic cosmetic;
}
