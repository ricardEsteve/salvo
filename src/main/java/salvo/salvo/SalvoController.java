package salvo.salvo;


import javafx.scene.control.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.lang.reflect.Method;
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
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @RequestMapping("/games")
    public Map<String, Object> getGamesAndPlayer(Authentication authentication) {
        Map<String, Object> map = new LinkedHashMap<>();

        if (!isGuest(authentication)) {
            Player currentPlayer = playerRepository.findByEmail(authentication.getName());
            map.put("player", getPlayerInfo(currentPlayer));
        }

        map.put("games", gameList());
        return map;
    }

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


    public Map<String, Object> getPlayerInfo(Player player) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", player.getId());
        map.put("name", player.getEmail());

        return map;
    }


    public Map<String, Object> gamePlayerMap(GamePlayer gamePlayer) {
        Map<String, Object> map = new HashMap<>();
        map.put("gpid", gamePlayer.getId());
        map.put("id", gamePlayer.getPlayer().getId());
        map.put("player", playerMap(gamePlayer.getPlayer(), gamePlayer.getGame()));

        return map;

    }

    public Map<String, Object> playerMap(Player player, Game game) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", player.getId());
        map.put("email", player.getEmail());
        if (player.getScore(game) != null) {
            map.put("score", player.getScore(game).getPoints());
        }


        return map;
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> createGame(Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<Object>("you are not logged in", HttpStatus.FORBIDDEN);
        } else {
            Game game = new Game();
            Date date = new Date();
            gameRepository.save(game);
            Player currentPlayer = playerRepository.findByEmail(authentication.getName());
            GamePlayer newGamePlayer = new GamePlayer(date, game, currentPlayer);
            gamePlayerRepository.save(newGamePlayer);


            Map<String, Object> map = new LinkedHashMap<>();
            map.put("gpid", newGamePlayer.getId());
            return new ResponseEntity<Object>(map, HttpStatus.CREATED);

        }


    }

    private GamePlayer getOponent (GamePlayer gamePlayer){


        Long gamePlayerId = gamePlayer.getId();
        Set<GamePlayer> gamePlayers= gamePlayer.getGame().getGamePlayers();

        GamePlayer Oponent = gamePlayers.stream().filter(gp -> gp.getId()!= gamePlayerId).findAny().orElse(null);
        return Oponent;

    }


    @RequestMapping(value = "/game_view/{gamePlayerId}")
    public ResponseEntity<Object> gamePlayerId(@PathVariable long gamePlayerId, Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Game theGame = gamePlayer.getGame();

        Map<String, Object> map = new LinkedHashMap<>();

        if (isGuest(authentication)) {
            return new ResponseEntity<Object>("you are not logged in", HttpStatus.FORBIDDEN);
        } else if (gamePlayer == null) {
            return new ResponseEntity<Object>("the gameplayer does not exists", HttpStatus.FORBIDDEN);
        }

        List <String> enemySalvos = listOfSalvoes(getOponent(gamePlayer));
        List <String> mySalvos = listOfSalvoes(gamePlayer);

        map.put("id", theGame.getId());
        map.put("created", theGame.getCreationDate());
        map.put("gamePlayers", theGame.getGamePlayers().stream().map(gp -> eachGamePlayerMap(gp))
                .collect(Collectors.toList()));

        Set<Ship> setOfShips = gamePlayer.getShips();
        map.put("ships", setOfShips.stream().map(ship -> mapOfShip(ship)).collect(Collectors.toList()));

        Set<GamePlayer> bothGamePlayers = theGame.getGamePlayers();

        map.put("salvoes", bothGamePlayers.stream().map(gamePlayer1 -> mapOfSalvo(gamePlayer1)).collect(Collectors.toList()));
        map.put("mySalvoes", gamePlayer.getSalvo().stream().map(salvo -> getMySalvoInfo(salvo)).collect(Collectors.toList()));
        map.put("hitAndSunk",gamePlayer.getShips().stream().map(ship -> hitAndSunk(enemySalvos, ship)).collect(Collectors.toList()));
        map.put("mySunk",getOponent(gamePlayer).getShips().stream().map(ship -> hitAndSunk(mySalvos, ship)).collect(Collectors.toList()));

        return new ResponseEntity<Object>(map, HttpStatus.CREATED);
    }

    public Map<String, Object> eachGamePlayerMap(GamePlayer gamePlayer) {



        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", gamePlayer.getId());
        map.put("email", gamePlayer.getPlayer().getEmail());

        return map;

    }

    public List <String> getShiplocation (GamePlayer gamePlayer){
        if (gamePlayer == null){
            return new ArrayList<>();
        }

        List <String> shipLocation = gamePlayer.getShips().stream().map(ship -> ship.getCells())
                .flatMap(cells -> cells.stream())
                .collect(Collectors.toList());
        return shipLocation;
    }

    public Map<String, Object> getMySalvoInfo (Salvo salvo){
        Map <String, Object> map= new LinkedHashMap<>();
        map.put("location",salvo.getCells() );
        map.put("turn", salvo.getTurn());
        map.put("hits", getHits(salvo));

        return  map;
    }

    public List <String> getHits (Salvo salvo){
        List<String> locationSalvo = salvo.getCells();
        List<String> locationOponentShip = getShiplocation(getOponent(salvo.getGamePlayer()));
        List<String> hits = locationSalvo.stream().filter(cell->locationOponentShip.contains(cell)).collect(Collectors.toList());

        return hits;
    }

    private Map<String, Object> hitAndSunk(List<String> enemySalvoes, Ship ship) {



        Map<String, Object> shipInfoSunkTypeMap = new LinkedHashMap<>();
        boolean shipIsSunk = ship.getCells().stream()
                .allMatch(locations -> enemySalvoes.contains(locations));

        shipInfoSunkTypeMap.put("typeShip", ship.getType());
        shipInfoSunkTypeMap.put("isSunk",shipIsSunk );

        return shipInfoSunkTypeMap;
    }

    public List<String> listOfSalvoes(GamePlayer gamePlayer) {

        if (gamePlayer == null){
            return new ArrayList<String>();
        }

        return gamePlayer.getSalvo().stream()
                .map(salvo -> salvo.getCells())
                .flatMap(cells -> cells.stream()).collect(Collectors.toList());
    }



    public Map<String, Object> mapOfShip(Ship ship) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("type", ship.getType());
        map.put("locations", ship.getCells());

        return map;

    }

    public List<Object> mapOfSalvo(GamePlayer gamePlayer) {

        List<Object> list = new ArrayList<>();
        Set<Salvo> setOfSalvoes = gamePlayer.getSalvo();

        for (Salvo salvo : setOfSalvoes) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("turn", salvo.getTurn());
            map.put("gamePlayerId", gamePlayer.getId());
            map.put("locations", salvo.getCells());
            list.add(map);
        }

        return list;
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> createPlayer(String userName, String email, String password) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        if (isEmailTaken(email)) {
            responseMap.put("error ", " Name in use");
            return new ResponseEntity<Object>(responseMap,
                    HttpStatus.FORBIDDEN);
        } else {
            Player newPlayer = new Player(userName, email, password);
            playerRepository.save(newPlayer);
            responseMap.put("welcome", userName);
            return new ResponseEntity<Object>(responseMap, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> gameId(@PathVariable Long gameId, Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<Object>("you are not logged in", HttpStatus.UNAUTHORIZED);
        }

        if (gameId == null) {
            return new ResponseEntity<Object>("the gameId does not exists", HttpStatus.FORBIDDEN);
        }

        Game gameToJoin = gameRepository.findOne(gameId);

        if (gameToJoin == null) {
            return new ResponseEntity<Object>("you cannot join the game", HttpStatus.UNAUTHORIZED);
        }
        if (gameToJoin.getGamePlayers().size() != 1) {
            return new ResponseEntity<Object>("the game is full", HttpStatus.UNAUTHORIZED);

        }

        Player joiningPlayer = playerRepository.findByEmail(authentication.getName());
        Player waitingPlayer = gameToJoin.getGamePlayers().stream().findFirst().orElse(null).getPlayer();
        if (joiningPlayer == waitingPlayer) {
            return new ResponseEntity<Object>("you are already waiting", HttpStatus.FORBIDDEN);
        }

        Date date = new Date();
        GamePlayer secondGamePlayer = new GamePlayer(date, gameToJoin, joiningPlayer);
        gamePlayerRepository.save(secondGamePlayer);

        Map<String, Object> NewGamePlayerIDMap = new LinkedHashMap<>();
        NewGamePlayerIDMap.put("gpId", secondGamePlayer.getId());
        return new ResponseEntity<Object>(NewGamePlayerIDMap, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Object> gamePlayerId(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> setOfShips) {
        if (isGuest(authentication)) {
            return new ResponseEntity<Object>("you are not logged in", HttpStatus.UNAUTHORIZED);
        }
        Player currentPlayer = playerRepository.findByEmail(authentication.getName());
        GamePlayer foundGamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Player ThePlayerInTheGp = gamePlayerRepository.findOne(gamePlayerId).getPlayer();
        if (foundGamePlayer == null) {
            return new ResponseEntity<Object>("there is no game player with the given ID", HttpStatus.UNAUTHORIZED);
        }
        if (ThePlayerInTheGp != currentPlayer) {
            return new ResponseEntity<Object>("the current user is not the game player the ID references", HttpStatus.UNAUTHORIZED);
        }

        Set<Ship> foundShip = gamePlayerRepository.findOne(gamePlayerId).getShips();
        if (!foundShip.isEmpty()) {
            return new ResponseEntity<Object>("the user already has ships placed", HttpStatus.FORBIDDEN);
        }
        for (Ship ship : setOfShips) {

            if(ship.getCells().size() == 5){
                ship.setType(ShipType.carrier);
            }

            if(ship.getCells().size() == 4){
                ship.setType(ShipType.battleship);
            }

            if(ship.getCells().size() == 3){
                ship.setType(ShipType.submarine);
            }

            if(ship.getCells().size() == 2){
                ship.setType(ShipType.destroyer);
            }

            if(ship.getCells().size() == 1){
                ship.setType(ShipType.patrolBoat);
            }

            ship.setGamePlayer(foundGamePlayer);
            shipRepository.save(ship);
        }
        return  new ResponseEntity<Object>("ships are be added to the game player and saved", HttpStatus.CREATED);
    }


    @RequestMapping(value = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Object> firedSalvos (Authentication authentication, @PathVariable Long gamePlayerId, @RequestBody Salvo salvo){
        if (isGuest(authentication)) {
            return new ResponseEntity<Object>("you are not logged in", HttpStatus.UNAUTHORIZED);
        }
        // Get the player who is logged
        Player currentPlayer = playerRepository.findByEmail(authentication.getName());
        GamePlayer foundGamePlayer = gamePlayerRepository.findOne(gamePlayerId);

        //Player ThePlayerInTheGp = gamePlayerRepository.findOne(gamePlayerId).getPlayer();
        if (foundGamePlayer == null) {
            return new ResponseEntity<Object>("there is no game player with the given ID", HttpStatus.UNAUTHORIZED);
        }

        Player playerOfGamePlayer = foundGamePlayer.getPlayer();

        if (playerOfGamePlayer.getId() != currentPlayer.getId()) {
            return new ResponseEntity<Object>("the current user is not the game player the ID references", HttpStatus.UNAUTHORIZED);
        }

        int theCurrentTurn = foundGamePlayer.theCurrentTurn();
        salvo.setGamePlayer(foundGamePlayer);
        salvo.setTurn(theCurrentTurn + 1);
        salvoRepository.save(salvo);

        return  new ResponseEntity<Object>(" the salvo should be added to the game player and saved",HttpStatus.CREATED);
    }



    public boolean isEmailTaken (String email){

        Player playerFound = playerRepository.findByEmail(email);
        return playerFound != null;
    }

    public boolean exists (Player newPlayer){

        boolean exists = false;

        for (Player player : playerRepository.findAll()){
            if (newPlayer.getEmail()== player.getEmail()){
                exists = true;
            }
        }
        return exists;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
}
