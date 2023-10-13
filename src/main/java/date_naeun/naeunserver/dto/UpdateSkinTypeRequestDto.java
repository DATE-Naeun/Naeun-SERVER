package date_naeun.naeunserver.dto;

import lombok.Data;

@Data
public class UpdateSkinTypeRequestDto {
        private String skinType;
        private Boolean trouble;
        private Boolean whitening;
}
