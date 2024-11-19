package ObligatorioDDA_IS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ObligatorioDDA_IS.Models.SinglePlayerGame;

public interface SPGameRepository extends JpaRepository<SinglePlayerGame, Integer> {
    List<SinglePlayerGame> findByUserId(Long userId);
}