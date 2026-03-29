package dreamdev.moniepoint.data.repositories;

import dreamdev.moniepoint.data.models.Election;
import dreamdev.moniepoint.utils.enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ElectionRepository extends MongoRepository<Election, String> {

    Optional<Election> findByName(String name);

    List<Election> findByStatus(Status status);

    boolean existsByName(String name);
}