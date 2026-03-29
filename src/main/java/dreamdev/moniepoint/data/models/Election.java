package dreamdev.moniepoint.data.models;

import dreamdev.moniepoint.utils.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document()
public class Election {
    @Id
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private Status status;
    private Integer totalVotes;
}
