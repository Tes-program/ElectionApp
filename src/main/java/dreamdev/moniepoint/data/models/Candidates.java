package dreamdev.moniepoint.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document()
public class Candidates {
    @Id
    private String id;
    private String name;
    private String electionId;
    private Integer voteCount;;
}
