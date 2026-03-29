package dreamdev.moniepoint.dtos.responses;

import dreamdev.moniepoint.utils.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ElectionResponse {
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private Status status;
    private Integer totalVotes;
}