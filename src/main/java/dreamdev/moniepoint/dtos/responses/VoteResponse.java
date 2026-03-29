package dreamdev.moniepoint.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteResponse {
    private String id;
    private String electionId;
    private String candidateId;
    private String voterId;
    private String createdAt;
}