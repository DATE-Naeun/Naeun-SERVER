package date_naeun.naeunserver.exception;

public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    INTERNAL_SERVER_ERROR(500);

    HttpStatusCode(int i) {
    }
}