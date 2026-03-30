package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Candidates;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultsServiceImpl implements ResultsService {

    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    @Override
    public List<CandidateResponse> getElectionResults(String electionId) {
        // Validate election exists
        electionRepository.findById(electionId)
                .orElseThrow(() -> new IllegalArgumentException("Election with id '" + electionId + "' does not exist"));

        // Get all candidates for the election
        List<Candidates> candidates = candidateRepository.findByElectionId(electionId);

        // Sort by voteCount descending and map to response
        return candidates.stream()
                .sorted(Comparator.comparing(Candidates::getVoteCount).reversed())
                .map(candidate -> new CandidateResponse(
                        candidate.getId(),
                        candidate.getName(),
                        candidate.getElectionId(),
                        candidate.getVoteCount()
                ))
                .collect(Collectors.toList());
    }
}
