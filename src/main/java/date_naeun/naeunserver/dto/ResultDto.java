package date_naeun.naeunserver.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data @RequiredArgsConstructor(staticName = "of")
public class ResultDto<D> {
    private final HttpStatus resultCode;
    private final String message;
    private final D data;
}
