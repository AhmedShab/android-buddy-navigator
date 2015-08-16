var http = require("http"),
	querystring = require("querystring"),
	socketio = require("socket.io");

const PORT = 8080;
var credentials = {};

var server = http.createServer(function (request, response) {
	response.writeHead(200, "Header", {"Content-Type": "application/x-www-form-urlencoded"});
	console.log("Connected.");
	request.on("data", function (data) {
		post = querystring.parse(data.toString());
		if (post["request"] == "register") {
			if (credentials.hasOwnProperty(post["username"]) == true)
				response.end("false");
			else {
				credentials[post["username"]] = post["password"];
				response.end("true");
			}
		} else if (post["request"] == "login") {
			if (credentials.hasOwnProperty(post["username"]) == true && credentials[post["username"]] == post["password"])
				response.end("true");
			else
				response.end("false");
		} else if (post["request"] == "friend") {
			if (credentials.hasOwnProperty(post["username"]) == true)
				response.end("true");
			else
				response.end("false");
		}
	});
}).listen(PORT, function () {
	console.log("Server listening at: http://localhost:" + PORT);
});

socketio.listen(server).on("connection", function (socket) {
	socket.on("location", function (data) {
		// This will be used for updating the cursor and sending data to the other guy
	});

	socket.on("error", function (err) {
		console.log("There is an error: " + err);
	});

	socket.on("disconnect", function () {
		
	});
});