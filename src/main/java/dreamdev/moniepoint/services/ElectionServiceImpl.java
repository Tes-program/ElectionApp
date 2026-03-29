package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElectionServiceImpl implements ElectionService {

    private final ElectionRepository electionRepository;

    @Override
    public ElectionResponse createElection(ElectionRequest request) {
        if (electionRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Election with name '" + request.getName() + "' already exists");
        }

        Election election = new Election();
        election.setName(request.getName());
        election.setStartDate(request.getStartDate());
        election.setEndDate(request.getEndDate());
        election.setStatus(Status.UPCOMING);
        election.setTotalVotes(0);

        Election savedElection = electionRepository.save(election);

        return new ElectionResponse(
                savedElection.getId(),
                savedElection.getName(),
                savedElection.getStartDate(),
                savedElection.getEndDate(),
                savedElection.getStatus(),
                savedElection.getTotalVotes()
        );
    }
}