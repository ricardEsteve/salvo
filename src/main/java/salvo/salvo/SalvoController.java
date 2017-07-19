package salvo.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;
import java.util.*;
import java.util.stream.Collectors;

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

            map.put("gameplayers", games.get(i).getGamePlayers()
                    .stream()
                    .map(gamePlayer -> gamePlayerMap(gamePlayer))
                    .collect(Collectors.toList()));

            list.add(map);

        }
      return list;
    }

    public Map<String, Object> gamePlayerMap (GamePlayer gamePlayer){
        Map<String, Object> map = new HashMap<>();
        map.put ("id", gamePlayer.getId());
        map.put ("player", PlayerMap(gamePlayer.getPlayer()));

        return map;

    }
    public Map<String, Object> PlayerMap (Player player) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", player.getId());
        map.put("email", player.getEmail());

        return map;
    }
}
