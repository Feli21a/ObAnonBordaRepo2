package ObligatorioDDA_IS.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ObligatorioDDA_IS.Models.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    List<Participant> findByUserId(Long userId);
}

