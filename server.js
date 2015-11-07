var http = require("http");
var querystring = require("querystring");

const PORT = 8080;
var userIDs = ["Jemboy"];

var server = http.createServer(function (request, response) {
	response.writeHead(200, "Header", {"Content-Type": "application/x-www-form-urlencoded"});

	request.on("data", function (data) {
		post = querystring.parse(data.toString());
		var request = post["request"];
		
		if (request == "upload") {
			var localID = post["localID"], pastID = post["pastID"];
			uploadFunction(response, localID, pastID);
		}

		if (request == "download") {
			var remoteID = post["remoteID"];
			downloadFunction(response, remoteID);
		}

		if (request == "delete") {
			var targetID = post["targetID"];
			deleteFunction(targetID);
		}
		console.log(userIDs);
	});
});

server.listen(PORT, function () {
	console.log("Server listening at: http://localhost:" + PORT);
});

var uploadFunction = function (response, localID, pastID) {
	if (userIDs.indexOf(localID) > -1) {
		response.end("0");
	}
	else {
		var pastID = post["pastID"];
		if (userIDs.indexOf(pastID) > -1)
			userIDs.splice(userIDs.indexOf(pastID), 1);
		userIDs.push(localID);
		response.end("1");
	}
};

var downloadFunction = function (response, remoteID) {
	if (userIDs.indexOf(remoteID) > -1) {
		response.end("1");
	}
	else {
		response.end("0");
	}
};

var deleteFunction = function (targetID) {
	if (userIDs.indexOf(targetID) > -1)
		userIDs.splice(userIDs.indexOf(targetID), 1);
};

/*
var jsonString = '[]';
var jsonArray = JSON.parse(jsonString);
jsonArray.push({"userId" : request["userId"], "latitude" : request["latitude"], "longitude" : request["longitude"]});
var jsonObject = {"latitude" : 40.891662, "longitude" : 29.378559};
response.end(JSON.stringify(jsonObject));
*/