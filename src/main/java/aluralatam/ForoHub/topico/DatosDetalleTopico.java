package aluralatam.ForoHub.topico;

import java.time.LocalDate;

public record DatosDetalleTopico(Long id,
                                 String titulo,
                                 String mensaje,
                                 LocalDate fechaDeCreacion,
                                 Status status,
                                 String autor,
                                 String curso) {

}
