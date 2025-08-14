package aluralatam.ForoHub.topico;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record DatosNuevoTopico(String id,
                               @NotBlank String titulo,
                               @NotBlank String mensaje,
                               @PastOrPresent LocalDate fechaDeCreacion,
                               @NotNull Status status,
                               @NotBlank String autor,
                               @NotBlank String curso) {
}
