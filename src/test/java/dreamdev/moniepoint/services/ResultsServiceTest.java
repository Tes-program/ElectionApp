package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.data.repositories.VoteRepository;
import dreamdev.moniepoint.dtos.requests.CandidateRequest;
import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.requests.VoteRequest;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.utils.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResultsServiceTest {

    @Autowired
    private ResultsService resultsService;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ElectionRepository electionRepository;

    private String electionId;
    private String candidate1Id;
    private String candidate2Id;
    private String candidate3Id;

    @BeforeEach
    void setUp() {
        voteRepository.deleteAll();
        candidateRepository.deleteAll();
        electionRepository.deleteAll();

        // Create election
        ElectionRequest electionRequest = new ElectionRequest();
        electionRequest.setName("Presidential Election 2026");
        electionRequest.setStartDate("2026-06-01");
        electionRequest.setEndDate("2026-06-30");
        ElectionResponse election = electionService.createElection(electionRequest);
        electionId = election.getId();

        // Set election to ONGOING
        Election ongoingElection = electionRepository.findById(electionId).get();
        ongoingElection.setStatus(Status.ONGOING);
        electionRepository.save(ongoingElection);

        // Create candidates
        CandidateRequest candidate1 = new CandidateRequest();
        candidate1.setName("Alice");
        candidate1.setElectionId(electionId);
        candidate1Id = candidateService.addCandidate(candidate1).getId();

        CandidateRequest candidate2 = new CandidateRequest();
        candidate2.setName("Bob");
        candidate2.setElectionId(electionId);
        candidate2Id = candidateService.addCandidate(candidate2).getId();

        CandidateRequest candidate3 = new CandidateRequest();
        candidate3.setName("Charlie");
        candidate3.setElectionId(electionId);
        candidate3Id = candidateService.addCandidate(candidate3).getId();
    }

    @Test
    void getElectionResults_returnsEmptyListWhenNoVotes() {
        // Act
        List<CandidateResponse> results = resultsService.getElectionResults(electionId);

        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());
        // All candidates should have 0 votes
        results.forEach(candidate -> assertEquals(0, candidate.getVoteCount()));
    }

    @Test
    void getElectionResults_returnsCandidatesOrderedByVoteCountDescending() {
        // Arrange - Cast votes for different candidates
        // Bob gets 5 votes
        for (int i = 0; i < 5; i++) {
            VoteRequest vote = new VoteRequest();
            vote.setElectionId(electionId);
            vote.setCandidateId(candidate2Id);
            vote.setVoterId("voter" + i);
            voteService.castVote(vote);
        }

        // Alice gets 3 votes
        for (int i = 5; i < 8; i++) {
            VoteRequest vote = new VoteRequest();
            vote.setElectionId(electionId);
            vote.setCandidateId(candidate1Id);
            vote.setVoterId("voter" + i);
            voteService.castVote(vote);
        }

        // Charlie gets 1 vote
        VoteRequest charlieVote = new VoteRequest();
        charlieVote.setElectionId(electionId);
        charlieVote.setCandidateId(candidate3Id);
        charlieVote.setVoterId("voter8");
        voteService.castVote(charlieVote);

        // Act
        List<CandidateResponse> results = resultsService.getElectionResults(electionId);

        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());

        // Verify ordering: Bob (5) -> Alice (3) -> Charlie (1)
        assertEquals("Bob", results.get(0).getName());
        assertEquals(5, results.get(0).getVoteCount());

        assertEquals("Alice", results.get(1).getName());
        assertEquals(3, results.get(1).getVoteCount());

        assertEquals("Charlie", results.get(2).getName());
        assertEquals(1, results.get(2).getVoteCount());
    }

    @Test
    void getElectionResults_withInvalidElection_throwsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            resultsService.getElectionResults("invalid-election-id");
        });
    }

    @Test
    void getElectionResults_withTiedVotes_returnsAllCandidates() {
        // Arrange - Cast equal votes for all candidates
        for (int i = 0; i < 3; i++) {
            VoteRequest vote1 = new VoteRequest();
            vote1.setElectionId(electionId);
            vote1.setCandidateId(candidate1Id);
            vote1.setVoterId("voter_alice_" + i);
            voteService.castVote(vote1);

            VoteRequest vote2 = new VoteRequest();
            vote2.setElectionId(electionId);
            vote2.setCandidateId(candidate2Id);
            vote2.setVoterId("voter_bob_" + i);
            voteService.castVote(vote2);

            VoteRequest vote3 = new VoteRequest();
            vote3.setElectionId(electionId);
            vote3.setCandidateId(candidate3Id);
            vote3.setVoterId("voter_charlie_" + i);
            voteService.castVote(vote3);
        }

        // Act
        List<CandidateResponse> results = resultsService.getElectionResults(electionId);

        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());
        // All should have 3 votes
        results.forEach(candidate -> assertEquals(3, candidate.getVoteCount()));
    }
}
