package ObligatorioDDA_IS.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ObligatorioDDA_IS.DTO.UserRegistrationDTO;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.UserRepository;

//Servicio que contiene la lógica de negocio para el registro y autenticación de usuarios.

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Verifica si el correo y nombre de usuario ya existen
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("El correo ya está en uso");
        }
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Verifica si las contraseñas coinciden
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        // Crea el objeto User y encripta la contraseña antes de guardar
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        return userRepository.save(user);
    }

    public User authenticateUser(String email, String pass) {
        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(pass, user.getPassword())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }
        return user;
    }
}
