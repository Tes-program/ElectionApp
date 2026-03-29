package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.utils.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ElectionServiceTest {

    @Autowired
    private ElectionService electionService;

    @Autowired
    private ElectionRepository electionRepository;

    private ElectionRequest validRequest;

    @BeforeEach
    void setUp() {
        electionRepository.deleteAll();
        validRequest = new ElectionRequest();
        validRequest.setName("Student Election 2026");
        validRequest.setStartDate("2026-06-01");
        validRequest.setEndDate("2026-06-30");
    }

    @Test
    void createElection_withValidData_savesElection() {
        // Arrange
        assertEquals(0L, electionRepository.count());

        // Act
        ElectionResponse response = electionService.createElection(validRequest);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Student Election 2026", response.getName());
        assertEquals("2026-06-01", response.getStartDate());
        assertEquals("2026-06-30", response.getEndDate());
        assertEquals(Status.UPCOMING, response.getStatus());
        assertEquals(0, response.getTotalVotes());
        assertEquals(1L, electionRepository.count());
    }

    @Test
    void createElection_withDuplicateName_throwsException() {
        // Arrange
        electionService.createElection(validRequest);
        assertEquals(1L, electionRepository.count());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            electionService.createElection(validRequest);
        });
        assertEquals(1L, electionRepository.count());
    }
}
