var fs = require("fs");
var http = require("http");
var dispatcher = require("httpdispatcher");

const PORT = 8080;

var server = http.createServer(function (request, response) {
	dispatcher.dispatch(request, response);
}).listen(PORT, function() {
	console.log("Server listening at: http://localhost:" + PORT);
});