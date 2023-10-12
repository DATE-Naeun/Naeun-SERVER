package date_naeun.naeunserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReviewRequestDto {

        private Long cosmeticId;
        private Double rating;
        private boolean isOpen;
        private String content;
        private String texture;
        private String repurchase;
        private List<String> photo;

}
