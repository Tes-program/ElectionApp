package dreamdev.moniepoint.dtos.requests;

import lombok.Data;

@Data
public class CandidateRequest {
    private String name;
    private String electionId;
    private String createdAt;
    private String updatedAt;
}
