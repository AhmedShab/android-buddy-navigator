var fs = require("fs");
var http = require("http");
var socketio = require("socket.io");

const PORT = 8080;

var server = http.createServer(function(request, response) {
	response.writeHead(200, {"Content-type": "text/html"});
	response.end();
}).listen(PORT, function() {
	console.log("Server listening at: http://localhost:" + PORT);
});

clients = {};
socketio.listen(server).on("connection", function(socket) {
	socket.on("register", function(data) {
		var username = data;
		if (clients.hasOwnProperty(username) == false) {
			clients[username] = socket;
			socket.emit("register_success");
		}  else {
			socket.emit("register_fail");
		}

	});

	socket.on("error", function(err) {
		console.log("There is an error: " + err);
	});

	socket.on("disconnect", function() {
		
	});
});