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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping ("/topicos")
public class TopicosController {

    @Autowired
    private ITopicoRepository repository;

    @Transactional
    @PostMapping
    public ResponseEntity nuevoTopico (@RequestBody @Valid DatosNuevoTopico datos, UriComponentsBuilder uriComponentsBuilder){

        var topico = new Topico(datos);
                repository.save(topico);

        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosDetalleTopico(topico));
    }

    @GetMapping
    public ResponseEntity<Page<DatosListaTopico>> listar (Pageable paginacion) {
       var page = repository.findAllByActivoTrue(paginacion).map(DatosListaTopico :: new);
       return ResponseEntity.ok(page);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<DatosDetalleTopico> detallarTopico (@PathVariable Long id){
        return repository.findById(id)
                .map(topico -> ResponseEntity.ok(topico.detalleTopicoSeleccionado()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping ("/{id}")
    public ResponseEntity actualizar (@PathVariable Long id,@RequestBody @Valid DatosActualizarTopico datos){
        var topico = repository.getReferenceById(id);

        topico.actualizarinformacion(datos);

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity eliminar(@PathVariable Long id){
        var topico = repository.getReferenceById(id);

        topico.eliminar();

        return ResponseEntity.noContent().build();

    }
}
