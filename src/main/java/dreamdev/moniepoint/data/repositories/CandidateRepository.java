package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Candidates;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends MongoRepository<Candidates, String> {

    List<Candidates> findByElectionId(String electionId);

    Optional<Candidates> findByNameAndElectionId(String name, String electionId);

    boolean existsByNameAndElectionId(String name, String electionId);
}