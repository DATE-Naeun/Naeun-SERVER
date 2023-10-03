package date_naeun.naeunserver.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class HarmfulDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "harm_id")
    private Long id;

    private String description;
    private Double weight;  // 기본 가중치

    //== 피부타입 및 피부고민 ==//
    private Double d;   // Dry
    private Double o;   // Oily
    private Double s;   // Sensitive
    private Double r;   // Resistant
    private Double p;   // Pigment
    private Double n;   // Non-pigment
    private Double t;   // Tight
    private Double w;   // Wrinkle
    private Double whitening;   // 미백
    private Double trouble;     // 트러블

    @Builder
    public HarmfulDetail(String description, Double weight, Double d, Double o, Double s, Double r, Double p, Double n, Double t, Double w, Double whitening, Double trouble) {
        this.description = description;
        this.weight = weight;
        this.d = d;
        this.o = o;
        this.s = s;
        this.r = r;
        this.p = p;
        this.n = n;
        this.t = t;
        this.w = w;
        this.whitening = whitening;
        this.trouble = trouble;
    }
}
