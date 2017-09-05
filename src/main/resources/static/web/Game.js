$(function () {

  var id = getURLParameterByName("gp");
  $.getJSON("/api/game_view/" + id, function (json) {
    console.log(json);

    createGrid(10);

    putShips(json.ships);

    showPlayer(json.gamePlayers);

    createSalvoGrid(10);

    putSalvoes(json.salvoes);


  });

});


function getURLParameterByName(name) {

  var url = window.location.href;
  name = name.replace(/[\[\]]/g, "\\$&");
  var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, " "));

}

function showPlayer(objectGamePlayers) {

  var list = document.getElementById("objectGamePlayers");
  var li = document.createElement("li");

  if (objectGamePlayers.length == 2) {
    var gp = getURLParameterByName("gp");
    if (objectGamePlayers[0].id == gp) {
      li.innerHTML = objectGamePlayers[0].email + " (you)" + " vs " + objectGamePlayers[1].email;
    } else {
      li.innerHTML = objectGamePlayers[0].email + " vs " + objectGamePlayers[1].email + " (you)";
    }
  } else {
    li.innerHTML = objectGamePlayers[0].email + "(you) waiting for an opponent";
  }

  list.append(li);
}


function createGrid(cellNumber) {
  var tbody = document.getElementById("tbl");
  var letters = [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
  var numbers = [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];


  for (var i = 0; i < cellNumber + 1; i++) {
    var row = document.createElement("tr");

    for (var j = 0; j < cellNumber + 1; j++) {
      var cell = document.createElement("td");
      if(i == 0){
            cell.innerHTML = numbers[j];
            }

            if(j == 0){
            cell.innerHTML = letters[i];
            }
      
    
      cell.className = "cell";
      cell.setAttribute("data-location", letters[i] + numbers[j]);
      row.appendChild(cell);
    }
    tbody.appendChild(row);

  }
}

function putShips(objectShips) {

  for (var i = 0; i < objectShips.length; i++) {
    for (var j = 0; j < objectShips[i].locations.length; j++) {
      var ocupedCell = $('[data-location="' + objectShips[i].locations[j] + '"]');
      ocupedCell.addClass("ocupedCell");
    }
  }
}

function createSalvoGrid(cellNumber) {
  var tbody = document.getElementById("tblsalvo");
  var letters = ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
  var numbers = ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];

  for (var i = 0; i < cellNumber + 1; i++) {
    var row = document.createElement("tr");

    for (var j = 0; j < cellNumber + 1; j++) {
      
      var cell = document.createElement("td");
      
      if(i == 0){
      cell.innerHTML = numbers[j];
      }
      
      if(j == 0){
      cell.innerHTML = letters[i];
      }
      cell.className = "cell";
      cell.setAttribute("data-locationOponent", letters[i] + numbers[j]);
      
      row.appendChild(cell);
    }
    tbody.appendChild(row);
  }
}

function putSalvoes(objectSalvoes) {

  objectSalvoes.forEach(function (player) {

    player.forEach(function (turn) {

      turn.locations.forEach(function (location) {
        if (turn.player == getURLParameterByName("gp")) {
          var firedCell = $('[data-location="' + location + '"]');
          
          if (firedCell.hasClass("ocupedCell")) {
            firedCell.addClass("firedCell crashCell");
            
          } else if(!firedCell.hasClass("ocupedCell")){
            firedCell.addClass("firedCell");
          }
            
          

          firedCell.html("<p class='shot'>" + turn.turn + "</p>");



        } else {

          var firedCellenemy = $('[data-locationOponent="' + location + '"]');
          console.log("Yes");
          firedCellenemy.addClass("firedCell");
          firedCellenemy.html("<p class='shot'>" + turn.turn + "</p>");
        }
      });

    });

  });
}
