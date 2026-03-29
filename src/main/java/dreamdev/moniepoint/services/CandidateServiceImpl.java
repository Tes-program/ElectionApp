package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Candidates;
import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.dtos.requests.CandidateRequest;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;
import dreamdev.moniepoint.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    @Override
    public CandidateResponse addCandidate(CandidateRequest request) {
        Election election = electionRepository.findById(request.getElectionId())
                .orElseThrow(() -> new IllegalArgumentException("Election with id '" + request.getElectionId() + "' does not exist"));

        if (election.getStatus() == Status.ENDED) {
            throw new IllegalArgumentException("Cannot add candidate to an ended election");
        }

        if (candidateRepository.existsByNameAndElectionId(request.getName(), request.getElectionId())) {
            throw new IllegalArgumentException("Candidate with name '" + request.getName() + "' already exists in this election");
        }

        Candidates candidate = new Candidates();
        candidate.setName(request.getName());
        candidate.setElectionId(request.getElectionId());
        candidate.setVoteCount(0);

        Candidates savedCandidate = candidateRepository.save(candidate);

        return new CandidateResponse(
                savedCandidate.getId(),
                savedCandidate.getName(),
                savedCandidate.getElectionId(),
                savedCandidate.getVoteCount()
        );
    }
}
