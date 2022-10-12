package lu.luxbank.restwebservice.exeption;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class LuxBankGeneralException extends RuntimeException {
    private HttpStatus status;
    private String errorMsg;

    public LuxBankGeneralException(HttpStatus status, String errorMsg) {
        super(errorMsg);
        this.status = status;
        this.errorMsg = errorMsg;
    }
}
