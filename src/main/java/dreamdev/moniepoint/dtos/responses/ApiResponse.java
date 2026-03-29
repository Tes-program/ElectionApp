package dreamdev.moniepoint.dtos.responses;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Object data;
}
