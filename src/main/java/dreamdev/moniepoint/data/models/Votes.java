package dreamdev.moniepoint.data.models;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document()
public class Votes {
    @Id
    private String id;
    private String electionId;
    private String candidateId;
    private String voterId;
    private String createdAt;
}
