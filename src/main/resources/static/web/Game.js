var sizeOfShip = 0;
var shipSelected;
var swichShip = false;
var myLetter = [" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
var myNumber = [" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];
var salvos = [];
var ships = [];
var gamePlayerId;


$(function () {

  gamePlayerId = getURLParameterByName("gp");

  var id = getURLParameterByName("gp");
  $.getJSON("/api/game_view/" + id, function (json) {
    console.log(json);

    createGrid(10);

    putShips(json.ships);

    showPlayer(json.gamePlayers);

    createSalvoGrid(10);

    putSalvoes(json.salvoes);

    if (json.ships.length > 0) {
      $("#sizeOfShips").hide();
    }




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

function signOut() {
  $.post("/api/logout").done(function () {
    location.replace("/web/games.html");
  });
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
      if (i == 0) {
        cell.innerHTML = numbers[j];
      }

      if (j == 0) {
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
  var tbody = document.getElementById("tblSalvo");
  var letters = ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"];
  var numbers = ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"];

  for (var i = 0; i < cellNumber + 1; i++) {
    var row = document.createElement("tr");

    for (var j = 0; j < cellNumber + 1; j++) {

      var cell = document.createElement("td");

      if (i == 0) {
        cell.innerHTML = numbers[j];
      }

      if (j == 0) {
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

  objectSalvoes.forEach(function (salvo) {

    salvo.forEach(function (turn) {

      turn.locations.forEach(function (location) {
        if (turn.player == getURLParameterByName("gp")) {
          var firedCell = $('[data-location="' + location + '"]');

          if (firedCell.hasClass("ocupedCell")) {
            firedCell.addClass("firedCell crashCell");

          } else if (!firedCell.hasClass("ocupedCell")) {
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

  //aquesta funcio ens porta als vaixells

  $("#locateShipsButton").click(locateShips);

  $("#fireSalvosButton").click(fireSalvos);

  $("td[data-locationoponent]").click(function () {
    var locationGrid = $(this).attr("data-locationoponent");
    var letter = locationGrid.substr(0, 1);
    var number = Number(locationGrid.substr(1));
    var cellId = $(this).attr("data-locationoponent");
    
    if (letter == "") {
      alert("cannot place anything there!");
      return;
    }

    if (number == "") {
      alert("cannot place anthing there!");
      return;

    }

    if (salvos.length >= 5) {
      alert("You need to shoot salvoes or reload");
      return;
    }
    
    if (salvos.indexOf(cellId) != -1) {
      var index = salvos.indexOf(cellId);
      
      salvos.splice(index, 1);
      $(this).toggleClass("firedCell");
    } else {
      salvos.push(cellId);
      $(this).toggleClass("firedCell");
    }

    
  });

  $("td[data-size]").click(function () {
    shipSelected = $(this);

    sizeOfShip = $(this).attr("data-size");
    swichShip = true;

  });

  //aquesta funcio ens porta a la graella
  $("td[data-location]").click(function () {
    if (swichShip == true) {
      var locationGrid = $(this).attr("data-location");
      var letter = locationGrid.substr(0, 1);
      var number = Number(locationGrid.substr(1));
      var locationForAship = [];

      //verticals
      if ($("#checkBoxButton").prop("checked")) {

        if (letter == " ") {
          alert("cannot place anything there!");
          return;
        }

        if (number == " ") {
          alert("cannot place anthing there!");
          return;

        }

        if (sizeOfShip == 5 && (letter == "G" || letter == "H" || letter == "I" || letter == "J")) {
          alert("not enough space!");
          return;
        }

        if (sizeOfShip == 4 && (letter == "H" || letter == "I" || letter == "J")) {
          alert("not enough space!");
          return;
        }
        if (sizeOfShip == 3 && (letter == "I" || letter == "J")) {
          console.log(letter);
          alert("not enough space!");
          return;
        }

        if (sizeOfShip == 2 && (letter == "J")) {
          alert("not enough space!");
          return;
        }


        for (var i = 0; i < sizeOfShip; i++) {
          var letterPosition = myLetter.indexOf(letter);
          var changingLetter = myLetter[letterPosition + i];
          var $shipCell = $("#tbl").find("[data-location =" + changingLetter + number + "]");

          if ($shipCell.hasClass("ocupedCell")) {
            alert("Ship alredy there!");
            return;
          }
        }
        // ja es pot colocar el ship

        // pinta totes les celes
        for (var i = 0; i < sizeOfShip; i++) {
          var letterPosition = myLetter.indexOf(letter);
          var changingLetter = myLetter[letterPosition + i];
          var $shipCell = $("#tbl").find("[data-location =" + changingLetter + number + "]");
          $shipCell.addClass("ocupedCell");

          locationForAship.push(changingLetter + number);


          console.log(locationForAship);
        }
        ships.push(locationForAship);
        console.log(ships);



        $(".ship" + sizeOfShip).hide();


      } else {
        //horitzontals
        if (number == " ") {
          alert("cannot place anything there!");
          return;

        }
        if (letter == " ") {
          alert("cannot place anything there!");
          return;

        }


        if (number > 6 && sizeOfShip == 5) {
          alert("not enough space!");
          return;
        }

        if (number > 7 && sizeOfShip == 4) {
          alert("not enough space!");
          return;
        }
        if (number > 8 && sizeOfShip == 3) {
          alert("not enough space!");
          return;
        }

        if (number > 9 && sizeOfShip == 2) {
          alert("not enough space!");
          return;
        }


        for (var i = 0; i < sizeOfShip; i++) {
          var myNumber = number + i;
          var $shipCell = $("#tbl").find("[data-location =" + letter + myNumber + "]");
          if ($shipCell.hasClass("ocupedCell")) {
            alert("Ship alredy there!");
            return;
          }

        }

        for (var i = 0; i < sizeOfShip; i++) {
          var myNumber = number + i;
          var $shipCell = $("#tbl").find("[data-location =" + letter + myNumber + "]");
          $shipCell.addClass("ocupedCell");

          locationForAship.push(letter + myNumber);
        }

        ships.push(locationForAship);


        console.log(ships);
        $(".ship" + sizeOfShip).hide();
      }
    }
    swichShip = false;
    //    $(this)swichShip.addClass("ocupedCell");
  });


  function locateShips() {
    console.log("/api/games/players/" + gamePlayerId + "/ships");
    var shipsIn = [];

    for (var i = 0; i < ships.length; i++) {
      var obj = {};
      obj.cells = ships[i];
      shipsIn.push(obj);
    }

    $.post({
        url: "/api/games/players/" + gamePlayerId + "/ships",
        contentType: "application/json",
        data: JSON.stringify(shipsIn)
      })
      .done(function () {
        alert("you have added a ship");
        $("#locateShips");

      });

  }

  function fireSalvos() {
    console.log("salvos to send:", salvos);
    $.post({
        url: "/api/games/players/" + gamePlayerId + "/salvos",
        contentType: "application/json",
        data: JSON.stringify({
          "cells": salvos
        }),


      })
      .done(function () {

        location.reload();
      });
  }
}

$("#goBackButton").click(goBack);

function goBack() {
  $.post("/api/games").done(function () {
    location.replace("/web/games.html");
  });
}
