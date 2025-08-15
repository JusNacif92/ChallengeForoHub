package aluralatam.ForoHub.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUsuarioRepositury extends JpaRepository<Usuario,Long> {

    UserDetails findByLogin(String login);
}
