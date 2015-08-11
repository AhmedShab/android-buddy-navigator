var http = require("http"),
	querystring = require("querystring");

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
		}
	});
}).listen(PORT, function() {
	console.log("Server listening at: http://localhost:" + PORT);
});

/*
switch (request.url) {
		case "/example":
			response.writeHead(200, "Stuff", {"Content Type": "application/x-www-form-urlencoded"});
			response.end();
			console.log("It works.");
			if (request.method == "POST") {
				request.on("data", function (data) {
					console.log("Data: " + data.toString());
				});
			}
			break;
		case "/exemplary":
			console.log("It really does.");
			break;
		default:
			console.log("Default.");
	};
*/