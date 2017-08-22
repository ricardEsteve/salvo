package salvo.salvo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by Ricard Esteve on 21/08/2017.
 */
@RepositoryRestResource public interface ScoreRepository extends JpaRepository<Score, Long>{

}



