var http = require("http");	

const PORT = 8080;
x = 50.0;
y = 99.5;

var server = http.createServer(function (request, response) {
	response.writeHead(200, "Header", {"Content-Type": "application/json"});
	console.log("Connected.");
	var jsonObject = {"latitude" : 40.891662, "longitude" : 29.378559};
	response.end(JSON.stringify(jsonObject));
}).listen(PORT, function () {
	console.log("Server listening at: http://localhost:" + PORT);
});