package salvo.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Ricard Esteve on 20/07/2017.
 */
public interface ShipRepository {

    @RepositoryRestResource
    public interface GameRepository extends JpaRepository<Ship, Long> {

    }
}
