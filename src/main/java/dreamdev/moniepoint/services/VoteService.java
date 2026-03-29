package dreamdev.moniepoint.services;

import dreamdev.moniepoint.dtos.requests.VoteRequest;
import dreamdev.moniepoint.dtos.responses.VoteResponse;

public interface VoteService {
    VoteResponse castVote(VoteRequest request);
}
