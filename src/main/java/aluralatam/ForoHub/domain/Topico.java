package aluralatam.ForoHub.domain;

import aluralatam.ForoHub.topico.DatosActualizarTopico;
import aluralatam.ForoHub.topico.DatosDetalleTopico;
import aluralatam.ForoHub.topico.DatosNuevoTopico;
import aluralatam.ForoHub.topico.Status;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Table (name = "topico")
@Entity (name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode ( of = "id")
public class Topico {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean activo;
    private String titulo;
    private String mensaje;
    @Column(name = "fechaDeCreacion")
    private LocalDate fechaDeCreacion;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String autor;
    private String curso;


    public Topico(DatosNuevoTopico datos) {

        this.id = null;
        this.activo = true;
        this.titulo = datos.titulo();
        this.mensaje = datos.mensaje();
        this.fechaDeCreacion = datos.fechaDeCreacion();
        this.status = datos.status();
        this.autor = datos.autor();
        this.curso = datos.curso();
    }

    //Para detallar un topico
    public DatosDetalleTopico detalleTopicoSeleccionado(){
        return new DatosDetalleTopico(
                this.id,
                this.titulo,
                this.mensaje,
                this.fechaDeCreacion,
                this.status,
                this.autor,
                this.curso
        );
    }

    public void actualizarinformacion(@Valid DatosActualizarTopico datos) {
        if (datos.titulo() != null) {
            this.titulo = datos.titulo();
        }
        if (datos.mensaje() != null) {
            this.mensaje = datos.mensaje();
        }
        if (datos.autor() != null) {
            this.autor = datos.autor();
        }
        if (datos.curso() != null) {
            this.curso = datos.curso();
        }
    }

    public void eliminar() {
        this.activo = false;
    }
}
