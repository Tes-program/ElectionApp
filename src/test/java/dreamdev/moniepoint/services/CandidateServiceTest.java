package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.repositories.CandidateRepository;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.dtos.requests.CandidateRequest;
import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.utils.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CandidateServiceTest {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private ElectionService electionService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ElectionRepository electionRepository;

    private CandidateRequest validRequest;
    private String electionId;

    @BeforeEach
    void setUp() {
        candidateRepository.deleteAll();
        electionRepository.deleteAll();

        // Create a valid election first
        ElectionRequest electionRequest = new ElectionRequest();
        electionRequest.setName("Presidential Election");
        electionRequest.setStartDate("2026-06-01");
        electionRequest.setEndDate("2026-06-30");
        ElectionResponse election = electionService.createElection(electionRequest);
        electionId = election.getId();

        validRequest = new CandidateRequest();
        validRequest.setName("John Doe");
        validRequest.setElectionId(electionId);
    }

    @Test
    void addCandidate_toValidElection_savesCandidate() {
        // Arrange
        assertEquals(0L, candidateRepository.count());

        // Act
        CandidateResponse response = candidateService.addCandidate(validRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals(electionId, response.getElectionId());
        assertEquals(0, response.getVoteCount());
        assertEquals(1L, candidateRepository.count());
    }

    @Test
    void addCandidate_toNonExistentElection_throwsException() {
        // Arrange
        validRequest.setElectionId("nonexistent-id");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            candidateService.addCandidate(validRequest);
        });
        assertEquals(0L, candidateRepository.count());
    }

    @Test
    void addCandidate_toEndedElection_throwsException() {
        // Arrange
        Election election = electionRepository.findById(electionId).get();
        election.setStatus(Status.ENDED);
        electionRepository.save(election);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            candidateService.addCandidate(validRequest);
        });
        assertEquals(0L, candidateRepository.count());
    }
}
