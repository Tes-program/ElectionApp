package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.requests.CandidateRequest;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;

public interface CandidateService {
    CandidateResponse addCandidate(CandidateRequest request);
}
