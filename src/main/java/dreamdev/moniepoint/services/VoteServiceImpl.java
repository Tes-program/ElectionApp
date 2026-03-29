package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Candidates;
import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.models.Votes;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.data.repositories.VoteRepository;
import dreamdev.moniepoint.dtos.requests.VoteRequest;
import dreamdev.moniepoint.dtos.responses.VoteResponse;
import dreamdev.moniepoint.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    @Override
    public VoteResponse castVote(VoteRequest request) {
        // Validate election exists
        Election election = electionRepository.findById(request.getElectionId())
                .orElseThrow(() -> new IllegalArgumentException("Election with id '" + request.getElectionId() + "' does not exist"));

        // Validate election is ONGOING
        if (election.getStatus() != Status.ONGOING) {
            throw new IllegalArgumentException("Election is not ongoing. Current status: " + election.getStatus());
        }

        // Validate candidate exists
        Candidates candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate with id '" + request.getCandidateId() + "' does not exist"));

        // Validate candidate belongs to election
        if (!candidate.getElectionId().equals(request.getElectionId())) {
            throw new IllegalArgumentException("Candidate does not belong to this election");
        }

        // Check voterId + electionId uniqueness (prevent duplicate voting)
        if (voteRepository.existsByVoterIdAndElectionId(request.getVoterId(), request.getElectionId())) {
            throw new IllegalArgumentException("Voter has already voted in this election");
        }

        // Create and save vote
        Votes vote = new Votes();
        vote.setElectionId(request.getElectionId());
        vote.setCandidateId(request.getCandidateId());
        vote.setVoterId(request.getVoterId());
        vote.setCreatedAt(LocalDateTime.now().toString());

        Votes savedVote = voteRepository.save(vote);

        // Increment candidate voteCount
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        candidateRepository.save(candidate);

        // Increment election totalVotes
        election.setTotalVotes(election.getTotalVotes() + 1);
        electionRepository.save(election);

        return new VoteResponse(
                savedVote.getId(),
                savedVote.getElectionId(),
                savedVote.getCandidateId(),
                savedVote.getVoterId(),
                savedVote.getCreatedAt()
        );
    }
}
