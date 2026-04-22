package dreamdev.moniepoint.controllers;

import dreamdev.moniepoint.dtos.requests.VoteRequest;
import dreamdev.moniepoint.dtos.responses.VoteResponse;
import dreamdev.moniepoint.services.VoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<VoteResponse> castVote(@RequestBody VoteRequest request) {
        VoteResponse response = voteService.castVote(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
