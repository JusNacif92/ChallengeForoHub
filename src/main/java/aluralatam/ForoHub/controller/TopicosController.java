package aluralatam.ForoHub.controller;

import aluralatam.ForoHub.domain.Topico;
import aluralatam.ForoHub.topico.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/topicos")
public class TopicosController {

    @Autowired
    private ITopicoRepository repository;

    @Transactional
    @PostMapping
    public void nuevoTopico (@RequestBody @Valid DatosNuevoTopico datos){
        repository.save(new Topico(datos));
    }

    @GetMapping
    public Page<DatosListaTopico> listar (Pageable paginacion) {
       return repository.findAllByActivoTrue(paginacion).map(DatosListaTopico :: new);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<DatosDetalleTopico> detallarTopico (@PathVariable Long id){
        return repository.findById(id)
                .map(topico -> ResponseEntity.ok(topico.detalleTopicoSeleccionado()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping ("/{id}")
    public void actualizar (@PathVariable Long id,@RequestBody @Valid DatosActualizarTopico datos){
        var topico = repository.getReferenceById(id);

        topico.actualizarinformacion(datos);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id){
        var topico = repository.getReferenceById(id);

        topico.eliminar();

    }
}
