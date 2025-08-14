package aluralatam.ForoHub.topico;

import aluralatam.ForoHub.domain.Topico;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;


public record DatosListaTopico(
        Long id,
        String titulo,
       String mensaje,
       LocalDate fechaDeCreacion,
       Status status,
       String autor,
       String curso) {

    public DatosListaTopico(Topico topico) {
        this(
                topico.getId(),
               topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaDeCreacion(),
                topico.getStatus(),
                topico.getAutor(),
                topico.getCurso()
        );
    }
}
