package ObligatorioDDA_IS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ObligatorioDDA_IS.Models.SinglePlayerGame;

public interface SPGameRepository extends JpaRepository<SinglePlayerGame, Integer> {
    
}