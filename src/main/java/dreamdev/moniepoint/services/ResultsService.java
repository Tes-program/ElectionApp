package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.responses.CandidateResponse;

import java.util.List;

public interface ResultsService {
    List<CandidateResponse> getElectionResults(String electionId);
}
