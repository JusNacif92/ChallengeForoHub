package aluralatam.ForoHub.topico;

import aluralatam.ForoHub.domain.Topico;

import java.time.LocalDate;

public record DatosDetalleTopico(Long id,
                                 String titulo,
                                 String mensaje,
                                 LocalDate fechaDeCreacion,
                                 Status status,
                                 String autor,
                                 String curso) {

    public DatosDetalleTopico(Topico topico) {
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
