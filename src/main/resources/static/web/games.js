$(function () {
  $("#logInButton").click(logIn);
  $("#signUpButton").click(signUp);
  $("#logOutButton").click(signOut);
  $("#newGameButton").click(newGame);
  $.getJSON("/api/games", onDataReady);
});


function onDataReady(json) {
  console.log(json);
  if (json.player == undefined) {
    $(".signIn").show();
    $(".signUp").show();


  } else {
    $("#logOutButton").show();
    $("#newGameButton").show();


  }
  playerList(json.games);
  //console.log(json.games);
  if(json.player) {
  createList(json);
  
}
}


function signOut() {
  $.post("/api/logout").done(function () {
    location.replace("/web/games.html");
  });
}

function signUp() {
  var userName = $("#name").val();
  var email = $("#newEmail").val();
  var password = $("#newPassword").val();
  var newUser = {
    "userName": userName,
    "email": email,
    "password": password
  };

  $("#logOutButton").show();
  $("#newGameButton").show();
  $("#logInButton").hide();
  $("#signUpButton").hide();

  $.post("/api/players", newUser).done(function () {
    var user = {
      "name": newUser.email,
      "password": newUser.password
    }
    $.post("/api/login", user).done(onLogIn);

  });

}

function logIn() {
  var email = $("#email").val();
  var password = $("#password").val();
  var user = {
    "name": email,
    "password": password
  };
  $.post("/api/login", user).done(onLogIn);

}

function onLogIn() {
  $("#logOutButton").show();
  $("#newGameButton").show();
  $(".data").hide();
  location.reload();
}

function newGame() {
  $.post("/api/games").done(function (response) {
    location.assign("/web/game.html?gp=" + response.gpid);
  });
}




function joinGame() {
  var gameId = this.id;
  $.post("/api/game/" + gameId + "/players").done(onJoinGame);
}

function onJoinGame(response){
//  console.log(response);
//  console.log(response.gpId);
  window.location.href = "game.html?gp=" + response.gpId; 
 
}



function createList(json) {
  console.log(json);
  
  var idGP;
  
  var games= json.games;
  var player=json.player;
  
  for (var i = 0; i < games.length; i++) {
    
    for(var j = 0; j < games[i].gameplayers.length; j++){
      if(games[i].gameplayers[j].id == json.player.id){
        idGP = games[i].gameplayers[j].gpid;
      }
    }
    
    var date = new Date(games[i].created).toLocaleString();
    var email1 = games[i].gameplayers[0].player.email;
    var email2;
    var button;
    if (games[i].gameplayers.length == 1) {
      email2 = "waiting player";
    } else {
      email2 = games[i].gameplayers[1].player.email;
    }
    if(games[i].gameplayers.length==1 && (player.name != email1 && player.name != email2)){
      button= "<button type='button'class='joinInButton' id='" + parseInt(games[i].Id) + "' >Join In</button>";
    }else if(email1 == player.name || email2==player.name){
      button= "<button type='button'class='playButton' id='" + idGP + "' >Play</button>";
    }else{
      button= "<button type='button'class='viewButton' id='" + games[i].Id + "' >View</button>";
      
    }
    
    
    
    var gameInfo = "<li>" + date + ": " + email1 + ", " + email2 + " " + button ;
    $("#list").append(gameInfo);
  }
  $(".joinInButton").click(joinGame);
  $(".playButton").click(function(){
    playGame(this);
  });

}

function playGame(button){
  var gpId= button.id;
  window.location.href = "game.html?gp=" + gpId; 
}


function onPlayGame(response){
  window.location.href = "game.html?gp=" + response.gpId; 
 
}

function playerList(games) {

  var results = {};

  for (var i = 0; i < games.length; i++) {
    for (var j = 0; j < games[i].gameplayers.length; j++) {
      var email = games[i].gameplayers[j].player.email;
      var score = games[i].gameplayers[j].player.score;

      if (!results.hasOwnProperty(email)) {
        results[email] = {
          email: email,
          total: 0,
          won: 0,
          lost: 0,
          tied: 0
        };
      }

      if (score != null) {
        results[email].total = results[email].total + score;
        if (score == 1) {
          results[email].won = results[email].won + 1;
        }
        if (score == 0) {
          results[email].lost = results[email].lost + 1;
        }
        if (score == 0.5) {
          results[email].tied = results[email].tied + 1;
        }

      }

    }

  }
  listInTable(results);
}

function listInTable(results) {

  //console.log(results);
  var tbody = document.getElementById("leaderBoard");

  var resultsArray = [];

  for (var mail in results) {
    resultsArray.push(results[mail]);
  }

  resultsArray.sort(function (a, b) {
    return b.total - a.total;

  });



  for (var index in resultsArray) {
    var currentPlayerData = resultsArray[index];
    var row = document.createElement("tr");

    emailCell = document.createElement("td");
    emailCell.className = "emailCell";
    emailCell.textContent = currentPlayerData.email;
    row.appendChild(emailCell);



    totalCell = document.createElement("td");
    totalCell.className = "totalCell";
    totalCell.textContent = currentPlayerData.total;
    row.appendChild(totalCell);


    wonCell = document.createElement("td");
    wonCell.className = "wonCell";
    wonCell.textContent = currentPlayerData.won;
    row.appendChild(wonCell);

    lostCell = document.createElement("td");
    lostCell.className = "lostCell";
    lostCell.textContent = currentPlayerData.lost;
    row.appendChild(lostCell);

    tiedCell = document.createElement("td");
    tiedCell.className = "tiedCell";
    tiedCell.textContent = currentPlayerData.tied;
    row.appendChild(tiedCell);

    //console.log("Key: " + mail + " / Value: ", results[mail].tied);
    tbody.appendChild(row);
  }

}
