package ObligatorioDDA_IS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ObligatorioDDA_IS.Models.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
}
