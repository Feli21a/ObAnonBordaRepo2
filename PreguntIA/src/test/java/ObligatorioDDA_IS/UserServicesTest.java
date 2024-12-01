package ObligatorioDDA_IS;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ObligatorioDDA_IS.DTO.UserRegistrationDTO;
import ObligatorioDDA_IS.Models.User;
import ObligatorioDDA_IS.Repository.UserRepository;
import ObligatorioDDA_IS.Services.UserService;
import jakarta.servlet.http.HttpSession;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Datos de prueba
        String email = "test@example.com";
        String username = "testuser";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);

        // Mockeo
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Registro
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO(username, email, password, password);
        User result = userService.registerUser(registrationDTO);

        // Validaciones
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_Success() {
        // Datos de prueba
        String email = "test@example.com";
        String password = "password123";

        User user = new User();
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(password));

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Llamar al servicio
        User result = userService.authenticateUser(email, password, session);

        // Validaciones
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(session, times(1)).setAttribute("user", result);
    }

    @Test
    void testGetUserProfile_Success() {
        // Datos de prueba
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setMaxScoreSP(100);

        when(session.getAttribute("user")).thenReturn(user);

        // Llamar al servicio
        Map<String, Object> profile = userService.getUserProfile(session);

        // Validaciones
        assertEquals("testuser", profile.get("username"));
        assertEquals(100, profile.get("maxScoreSP"));
    }
}
