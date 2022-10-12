package lu.luxbank.restwebservice.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ExceptionInfo {
    private String errorMsg;
}
