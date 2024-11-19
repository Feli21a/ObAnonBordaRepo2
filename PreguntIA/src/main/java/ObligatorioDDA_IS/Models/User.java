package ObligatorioDDA_IS.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

//Entidad que representa al usuario en la base de datos.

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
    private String username;

    @NotNull(message = "El correo no puede estar vacío")
    @Email(message = "Debe ingresar un correo electrónico válido")
    private String email;

    @NotNull(message = "La contraseña no puede estar vacía")
    private String password;

    private int maxScoreSP = 0;

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

    public void setMaxScoreSP(int score) {
        this.maxScoreSP = score;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxScoreSP() {
        return maxScoreSP;
    }

}
