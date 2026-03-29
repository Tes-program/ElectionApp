package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.utils.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElectionServiceTest {

    @Mock
    private ElectionRepository electionRepository;

    @InjectMocks
    private ElectionServiceImpl electionService;

    private ElectionRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new ElectionRequest();
        validRequest.setName("Student Election 2026");
        validRequest.setStartDate("2026-06-01");
        validRequest.setEndDate("2026-06-30");
    }

    @Test
    void createElection_withValidData_savesElection() {
        // Arrange
        when(electionRepository.existsByName(validRequest.getName())).thenReturn(false);

        Election savedElection = new Election();
        savedElection.setId("1");
        savedElection.setName(validRequest.getName());
        savedElection.setStartDate(validRequest.getStartDate());
        savedElection.setEndDate(validRequest.getEndDate());
        savedElection.setStatus(Status.UPCOMING);
        savedElection.setTotalVotes(0);

        when(electionRepository.save(any(Election.class))).thenReturn(savedElection);

        // Act
        ElectionResponse response = electionService.createElection(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Student Election 2026", response.getName());
        assertEquals("2026-06-01", response.getStartDate());
        assertEquals("2026-06-30", response.getEndDate());
        assertEquals(Status.UPCOMING, response.getStatus());
        assertEquals(0, response.getTotalVotes());

        verify(electionRepository, times(1)).existsByName(validRequest.getName());
        verify(electionRepository, times(1)).save(any(Election.class));
    }

    @Test
    void createElection_withEndDateBeforeStartDate_throwsException() {
        // Arrange
        ElectionRequest duplicateRequest = new ElectionRequest();
        duplicateRequest.setName("Existing Election");
        duplicateRequest.setStartDate("2026-06-01");
        duplicateRequest.setEndDate("2026-06-30");

        when(electionRepository.existsByName(duplicateRequest.getName())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            electionService.createElection(duplicateRequest);
        });

        verify(electionRepository, times(1)).existsByName(duplicateRequest.getName());
        verify(electionRepository, never()).save(any(Election.class));
    }
}
