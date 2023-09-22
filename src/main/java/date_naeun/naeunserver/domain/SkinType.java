package date_naeun.naeunserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 피부타입 엔티티
 */
@Getter @Setter
@Entity
@Table(name = "SkinType")
public class SkinType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skinType_id")
    private Long id;
    private String typeName;

    private String detail;

    @ElementCollection
    @CollectionTable(name = "skin_type_strong_points", joinColumns = @JoinColumn(name = "skin_type_id"))
    private List<String> strongPoint;

    @ElementCollection
    @CollectionTable(name = "skin_type_weak_points", joinColumns = @JoinColumn(name = "skin_type_id"))
    private List<String> weakPoint;

    @ElementCollection
    @CollectionTable(name = "skin_type_care", joinColumns = @JoinColumn(name = "skin_type_id"))
    private List<String> care;

    @OneToMany(mappedBy = "skinType", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

}
