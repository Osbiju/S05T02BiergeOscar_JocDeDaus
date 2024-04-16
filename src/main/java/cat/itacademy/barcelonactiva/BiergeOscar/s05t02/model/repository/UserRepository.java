package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
}
