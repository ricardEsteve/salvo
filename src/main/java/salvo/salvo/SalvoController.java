package salvo.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @RequestMapping("/games")
    public List<Object> gameList() {
        List<Object> list = new ArrayList<>();

        List<Game> games = gameRepository.findAll();


        for (int i = 0; i < games.size(); i++) {
            Map<String, Object> map = new LinkedHashMap<>();
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

    public Map<String, Object> gamePlayerMap(GamePlayer gamePlayer) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", gamePlayer.getId());
        map.put("player", playerMap(gamePlayer.getPlayer()));

        return map;

    }

    public Map<String, Object> playerMap(Player player) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", player.getId());
        map.put("email", player.getEmail());

        return map;
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> gamePlayerId ( @PathVariable long gamePlayerId){
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Game theGame = gamePlayer.getGame();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", theGame.getId());
        map.put("created", theGame.getCreationDate());
        map.put("gameplayers", theGame.getGamePlayers().stream().map(gp -> eachGamePlayerMap(gp))
                .collect(Collectors.toList()));

        Set<Ship> setOfShips = gamePlayer.getShips();
        map.put("ships", setOfShips.stream().map(Ship -> mapOfShip(Ship)).collect(Collectors.toList()));
        return map;
    }



    public Map<String, Object> eachGamePlayerMap (GamePlayer gamePlayer){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", gamePlayer.getId());
        map.put("email", gamePlayer.getPlayer().getEmail());

        return map;

    }

    public Map<String, Object> mapOfShip (Ship ship) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type",ship.getType() );
        map.put("locations", ship.getCells());

        return map;

    }



}
