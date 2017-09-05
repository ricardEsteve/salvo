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
  //console.log(json.games);
  createList(json.games);
  playerList(json.games);
}



function signOut() {
  $.post("/api/logout").done(function() {
    location.reload();
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

function newGame(){
  $.post("/api/games").done(function(response){
  } );
}

function createList(games) {
  for (var i = 0; i < games.length; i++) {
    var date = new Date(games[i].created).toLocaleString();
    var email1 = games[i].gameplayers[0].player.email;
    var email2;
    if (games[i].gameplayers.length == 1) {
      email2 = "Waiting player";
    } else {
      email2 = games[i].gameplayers[1].player.email;

    }


    var gameInfo = "<li>" + date + ": " + email1 + ", " + email2;
    $("#list").append(gameInfo);
  }


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

  })



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
