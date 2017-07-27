$(function () {

  var id = getURLParameterByName("gp");
  $.getJSON("/api/game_view/" + id, function (json) {



    createGrid(10);

    putShips(json.ships);

    showPlayer(json.gameplayers);

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
  var list = document.getElementById("objectPlayers");
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
      cell.className = "cell";
      cell.setAttribute("data-location", letters[i] + numbers[j]);
      row.appendChild(cell);
    }
    tbody.appendChild(row);

  }
}

function putShips(objectShips) {

  for (var i = 0; i < objectShips.length; i++) {

    //    console.log(objectShips[i].locations);

    for (var j = 0; j < objectShips[i].locations.length; j++) {
      //      console.log(objectShips[i].locations[j]);
      var ocupedCell = $('[data-location="' + objectShips[i].locations[j] + '"]');
      ocupedCell.addClass("ocupedCell");
    }
  }
}
