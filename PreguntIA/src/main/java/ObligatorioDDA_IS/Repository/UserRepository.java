package ObligatorioDDA_IS.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ObligatorioDDA_IS.Models.User;

//Repositorio que proporciona operaciones CRUD para la entidad User.

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String correo);
    boolean existsByUsername(String nombreUsuario);
    User findByEmail(String correo);
}