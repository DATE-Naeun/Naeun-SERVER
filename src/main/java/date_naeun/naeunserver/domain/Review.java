package date_naeun.naeunserver.domain;

import lombok.Getter;

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

    private double rating;
    private boolean isOpen;
    private Date date;

    private String content;
    private String texture;
    private String repurchase;

    @ElementCollection
    @CollectionTable(name = "review_image_list", joinColumns = @JoinColumn(name = "review_id"))
    private List<String> image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "csmt_id")
    private Cosmetic cosmetic;
}
