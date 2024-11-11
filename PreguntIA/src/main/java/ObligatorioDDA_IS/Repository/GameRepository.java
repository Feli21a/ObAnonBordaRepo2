package ObligatorioDDA_IS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ObligatorioDDA_IS.Models.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {
    
}