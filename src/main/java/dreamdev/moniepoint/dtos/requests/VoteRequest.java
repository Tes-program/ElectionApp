package dreamdev.moniepoint.dtos.requests;

import lombok.Data;

@Data
public class VoteRequest {
    private String candidateId;
    private String voterId;
    private String electionId;
    private String createdAt;
}
