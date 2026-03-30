package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.services.ElectionService;
import dreamdev.moniepoint.services.ResultsService;
import dreamdev.moniepoint.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elections")
@RequiredArgsConstructor
public class ElectionController {

    private final ElectionService electionService;
    private final ResultsService resultsService;

    @PostMapping
    public ResponseEntity<ElectionResponse> createElection(@RequestBody ElectionRequest request) {
        ElectionResponse response = electionService.createElection(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectionResponse> getElectionById(@PathVariable String id) {
        ElectionResponse response = electionService.getElectionById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ElectionResponse> updateElectionStatus(
            @PathVariable String id,
            @RequestBody StatusUpdateRequest statusRequest) {
        ElectionResponse response = electionService.updateElectionStatus(id, statusRequest.getStatus());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<CandidateResponse>> getElectionResults(@PathVariable String id) {
        List<CandidateResponse> results = resultsService.getElectionResults(id);
        return ResponseEntity.ok(results);
    }

    @lombok.Data
    public static class StatusUpdateRequest {
        private Status status;
    }
}
