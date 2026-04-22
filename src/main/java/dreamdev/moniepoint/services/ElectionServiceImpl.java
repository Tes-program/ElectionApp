package dreamdev.moniepoint.services;

import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.data.repositories.ElectionRepository;
import dreamdev.moniepoint.dtos.requests.ElectionRequest;
import dreamdev.moniepoint.dtos.responses.ElectionResponse;
import dreamdev.moniepoint.utils.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ElectionResponse getElectionById(String id) {
        Election election = electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election with id '" + id + "' does not exist"));

        return new ElectionResponse(
                election.getId(),
                election.getName(),
                election.getStartDate(),
                election.getEndDate(),
                election.getStatus(),
                election.getTotalVotes()
        );
    }

    @Override
    public ElectionResponse updateElectionStatus(String id, Status status) {
        Election election = electionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Election with id '" + id + "' does not exist"));

        election.setStatus(status);
        Election updatedElection = electionRepository.save(election);

        return new ElectionResponse(
                updatedElection.getId(),
                updatedElection.getName(),
                updatedElection.getStartDate(),
                updatedElection.getEndDate(),
                updatedElection.getStatus(),
                updatedElection.getTotalVotes()
        );
    }

    @Override
    public List<ElectionResponse> getAllElections(Status status) {
        List<Election> elections;

        if (status != null) {
            elections = electionRepository.findByStatus(status);
        } else {
            elections = electionRepository.findAll();
        }

        return elections.stream()
                .map(election -> new ElectionResponse(
                        election.getId(),
                        election.getName(),
                        election.getStartDate(),
                        election.getEndDate(),
                        election.getStatus(),
                        election.getTotalVotes()
                ))
                .collect(Collectors.toList());
    }
}