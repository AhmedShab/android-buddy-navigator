var fs = require("fs");
var http = require("http");
var socketio = require("socket.io");

const PORT = 8080;

var server = http.createServer(function(request, response) {
	response.writeHead(200, {"Content-type": "application/json"});
	var array = ["merkel", "jemboy", "putin"];
	var json = JSON.stringify({
		usernames: array
	});
	response.end(json);
}).listen(PORT, function() {
	console.log("Server listening at: http://localhost:" + PORT);
});

clients = {};		// Holds currently online clients

/**
 * Passwords hold all registered clients
 * I added some users here so there is no need to register each time to test features
 */
passwords = {"merkel": "123456", "putin": "123456", "obama": "123456"};		// Holds all registered clients
socketio.listen(server).on("connection", function(socket) {
	socket.on("register", function(data) {
		if (passwords.hasOwnProperty(data.username) == false) {
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

	socket.on("add_friend", function(data) {
		if (passwords.hasOwnProperty(data) == true) {
			console.log("Success!")
			socket.emit("add_friend_success");
		}
	});

	socket.on("error", function(err) {
		console.log("There is an error: " + err);
	});

	socket.on("disconnect", function() {
		
	});
});