package date_naeun.naeunserver.dto;

import date_naeun.naeunserver.exception.HttpStatusCode;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor(staticName = "of")
public class ResultDto<D> {
    private final HttpStatusCode code;
    private final String message;
    private final D data;
}
