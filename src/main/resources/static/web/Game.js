$(function () {

  var id = getParameterByName("gp");
  $.getJSON("/api/game_view/" + id, function (json) {



    createGrid(10);
    
    putShips ();
    

  });


});

function getParameterByName(name) {

  var url = window.location.href;
  name = name.replace(/[\[\]]/g, "\\$&");
  var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}




function createGrid(cellNumber) {
  var tbody = document.getElementById("tbl");
  var letters = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
  var numbers = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];


  for (var i = 0; i < cellNumber; i++) {
    var row = document.createElement("tr");

    for (var j = 0; j < cellNumber; j++) {
      var cell = document.createElement("td");
      cell.className = "cell";
      cell.setAttribute("data-location", letters[i] + numbers[j]);
      row.appendChild(cell);
    }
    tbody.appendChild(row);

  }
}

function putShips(){
  
}

