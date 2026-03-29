package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Candidates;
import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.data.repositories.VoteRepository;
import dreamdev.moniepoint.dtos.requests.CandidateRequest;
import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.requests.VoteRequest;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.dtos.responses.VoteResponse;
import dreamdev.moniepoint.utils.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private String electionId;
    private String candidateId;
    private VoteRequest validVoteRequest;

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

        // Set election to ONGOING status
        Election ongoingElection = electionRepository.findById(electionId).get();
        ongoingElection.setStatus(Status.ONGOING);
        electionRepository.save(ongoingElection);

        // Create candidate
        CandidateRequest candidateRequest = new CandidateRequest();
        candidateRequest.setName("John Doe");
        candidateRequest.setElectionId(electionId);
        CandidateResponse candidate = candidateService.addCandidate(candidateRequest);
        candidateId = candidate.getId();

        // Create valid vote request
        validVoteRequest = new VoteRequest();
        validVoteRequest.setElectionId(electionId);
        validVoteRequest.setCandidateId(candidateId);
        validVoteRequest.setVoterId("voter123");
    }

    @Test
    void castVote_inOngoingElection_savesVote() {
        // Arrange
        assertEquals(0L, voteRepository.count());
        Candidates candidate = candidateRepository.findById(candidateId).get();
        Election election = electionRepository.findById(electionId).get();
        int initialCandidateVotes = candidate.getVoteCount();
        int initialElectionVotes = election.getTotalVotes();

        // Act
        VoteResponse response = voteService.castVote(validVoteRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(electionId, response.getElectionId());
        assertEquals(candidateId, response.getCandidateId());
        assertEquals("voter123", response.getVoterId());
        assertNotNull(response.getCreatedAt());
        assertEquals(1L, voteRepository.count());

        // Verify candidate voteCount incremented
        Candidates updatedCandidate = candidateRepository.findById(candidateId).get();
        assertEquals(initialCandidateVotes + 1, updatedCandidate.getVoteCount());

        // Verify election totalVotes incremented
        Election updatedElection = electionRepository.findById(electionId).get();
        assertEquals(initialElectionVotes + 1, updatedElection.getTotalVotes());
    }

    @Test
    void castVote_twice_throwsDuplicateVoteException() {
        // Arrange
        voteService.castVote(validVoteRequest);
        assertEquals(1L, voteRepository.count());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            voteService.castVote(validVoteRequest);
        });
        assertEquals(1L, voteRepository.count());
    }

    @Test
    void castVote_inEndedElection_throwsException() {
        // Arrange
        Election election = electionRepository.findById(electionId).get();
        election.setStatus(Status.ENDED);
        electionRepository.save(election);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            voteService.castVote(validVoteRequest);
        });
        assertEquals(0L, voteRepository.count());
    }

    @Test
    void castVote_withInvalidCandidate_throwsException() {
        // Arrange
        validVoteRequest.setCandidateId("invalid-candidate-id");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            voteService.castVote(validVoteRequest);
        });
        assertEquals(0L, voteRepository.count());
    }

    @Test
    void castVote_withInvalidElection_throwsException() {
        // Arrange
        validVoteRequest.setElectionId("invalid-election-id");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            voteService.castVote(validVoteRequest);
        });
        assertEquals(0L, voteRepository.count());
    }
}
