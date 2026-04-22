package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.requests.CandidateRequest;
import dreamdev.moniepoint.dtos.responses.CandidateResponse;
import dreamdev.moniepoint.services.CandidateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public ResponseEntity<CandidateResponse> addCandidate(@RequestBody CandidateRequest request) {
        CandidateResponse response = candidateService.addCandidate(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
