package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
import java.util.*;

/**
 * Created by Ricard Esteve on 18/07/2017.
 */

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @RequestMapping("/games")
    public List <Object> gameList () {
        List<Object> list = new ArrayList<>();

        List<Game> games = gameRepository.findAll();



        for (int i = 0; i < games.size(); i++ ) {
            Map <String, Object> map = new LinkedHashMap<>();
            map.put("Id", games.get(i).getId());
            map.put("created", games.get(i).getCreationDate());
            list.add(map);

        }

        return list;
    }


}
