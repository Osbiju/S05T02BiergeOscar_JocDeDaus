package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.repository;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    boolean existsByPlayerName(String playerName);

}
