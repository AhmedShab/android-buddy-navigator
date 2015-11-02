var http = require("http");	

const PORT = 8080;

var userIDs = ["Jemboy"];

var server = http.createServer(function(request, response) {
	response.writeHead(200, "Header", {"Content-Type": "application/json"});
	console.log("Connected.");

	request.on("data", function(data) {
		var request = post["request"];
		if (request == "upload") {
			console.log("I am in UPLOAD().");
			var localID = post["localID"];
			if (userIDs.hasOwnProperty(localID)) {
				console.log("Fail");
				response.end("Fail");
			}
			else {
				console.log("Success");
				userIDs.push(localID);
				response.end("Success");
			}
		}
	});





	
}).listen(PORT, function () {
	console.log("Server listening at: http://localhost:" + PORT);
});

/*
var jsonString = '[]';
var jsonArray = JSON.parse(jsonString);
jsonArray.push({"userId" : request["userId"], "latitude" : request["latitude"], "longitude" : request["longitude"]});
var jsonObject = {"latitude" : 40.891662, "longitude" : 29.378559};
response.end(JSON.stringify(jsonObject));
*/