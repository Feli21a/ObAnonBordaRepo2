package ObligatorioDDA_IS.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//DTO para manejar los datos del registro de usuario, incluyendo la confirmación de contraseña.

public class UserRegistrationDTO {
    @NotNull(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @NotNull(message = "El correo no puede estar vacío")
    @Email(message = "Debe ingresar un correo electrónico válido")
    private String email;

    @NotNull(message = "La contraseña no puede estar vacía")
    private String password;

    @NotNull(message = "Debe confirmar la contraseña")
    private String confirmPassword;

    public UserRegistrationDTO(
            @NotNull(message = "El nombre de usuario no puede estar vacío") @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres") String username,
            @NotNull(message = "El correo no puede estar vacío") @Email(message = "Debe ingresar un correo electrónico válido") String email,
            @NotNull(message = "La contraseña no puede estar vacía") String password,
            @NotNull(message = "Debe confirmar la contraseña") String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
