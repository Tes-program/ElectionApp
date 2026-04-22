package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.utils.enums.Status;

import java.util.List;

public interface ElectionService {
    ElectionResponse createElection(ElectionRequest request);
    ElectionResponse getElectionById(String id);
    ElectionResponse updateElectionStatus(String id, Status status);
    List<ElectionResponse> getAllElections(Status status);
}