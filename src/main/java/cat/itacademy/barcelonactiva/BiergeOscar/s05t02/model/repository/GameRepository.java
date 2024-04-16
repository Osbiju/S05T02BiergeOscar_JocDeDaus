package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findByPlayerId(int id);
    void deleteByPlayerId(int id);
}
