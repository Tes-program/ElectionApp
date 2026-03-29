package dreamdev.moniepoint.dtos.requests;

import dreamdev.moniepoint.utils.enums.Status;
import lombok.Data;


@Data
public class ElectionRequest {
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private Status status;
    private Integer totalVotes;
}
