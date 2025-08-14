package aluralatam.ForoHub.topico;

import aluralatam.ForoHub.domain.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ITopicoRepository extends JpaRepository<Topico,Long> {

    Page<Topico> findAllByActivoTrue(Pageable paginacion);
}
