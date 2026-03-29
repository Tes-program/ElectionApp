package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;

public interface ElectionService {
    ElectionResponse createElection(ElectionRequest request);
}