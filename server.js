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
passwords = {};
socketio.listen(server).on("connection", function(socket) {
	socket.on("register", function(data) {
		if (clients.hasOwnProperty(data.username) == false) {
			clients[data.username] = socket;
			passwords[data.username] = data.password;
			socket.emit("register_success");
		}  else {
			socket.emit("register_fail");
		}
	});

	socket.on("login", function(data) {
		if (passwords.hasOwnProperty(data.username) == true) {
			if (passwords[data.username] == data.password) {
				socket.emit("login_success");
			} else {
				socket.emit("incorrect_password");
			}
		} else {
			socket.emit("incorrect_username");
		}
	});

	socket.on("error", function(err) {
		console.log("There is an error: " + err);
	});

	socket.on("disconnect", function() {
		
	});
});