package dreamdev.moniepoint.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateResponse {
    private String id;
    private String name;
    private String electionId;
    private Integer voteCount;
}