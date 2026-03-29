package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Votes;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteRepository extends MongoRepository<Votes, String> {

    List<Votes> findByElectionId(String electionId);

    List<Votes> findByCandidateId(String candidateId);

    boolean existsByVoterIdAndElectionId(String voterId, String electionId);

    int countByCandidateId(String candidateId);

    int countByElectionId(String electionId);
}