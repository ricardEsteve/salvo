$(function () {
  
  $.getJSON("/api/games", function (json) {
    
    createList(json);

  });
});

function createList(games) {
  for (var i = 0; i < games.length; i++) {
    var date =  new Date(games[i].created).toLocaleString();
    var email1 = games[i].gameplayers[0].player.email;
    var email2= games[i].gameplayers[1].player.email;
    
    var gameInfo = "<li>" + date + ": " + email1 + ", " + email2;
    $("#list").append(gameInfo);
  }

}
