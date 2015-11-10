var http = require("http");
var querystring = require("querystring");

const PORT = 8080;
var userIDs = 	[
					{"username" : "Jemboy", "latitude" : 0.0, "longitude" : 0.0},
					{"username" : "Obama", "latitude" : 10.0, "longitude" : 10.0}
				];

var server = http.createServer(function (request, response) {
	response.writeHead(200, "Header", {"Content-Type": "application/x-www-form-urlencoded"});

	request.on("data", function (data) {
		var post = querystring.parse(data.toString());
		var request = post["request"];
		
		if (request == "uploadID") {
			var localID = post["localID"];
			uploadFunction(response, localID);
		}

		if (request == "downloadID") {
			var remoteID = post["remoteID"];
			downloadFunction(response, remoteID);
		}

		if (request == "deleteID") {
			var targetID = post["targetID"];
			deleteFunction(targetID);
		}

		if (request == "uploadCoord") {
			
		}

		if (request == "downloadCoord") {

		}

		console.log(userIDs);
	});
});

server.listen(PORT, function () {
	console.log("Server listening at: http://localhost:" + PORT);
});

var uploadFunction = function (response, localID) {
	var success = true;
	for (var i = 0; i < userIDs.length; i++) {
		if (userIDs[i].username == localID) {
			success = false;
			response.end("0");
			break;			
		}
	}
	if (success == true) {
		var jsonObject = new Object();
		jsonObject["username"] = localID;
		jsonObject["latitude"] = 0.0;
		jsonObject["longitude"] = 0.0;
		userIDs.push(jsonObject);
		response.end("1");
	}
};

var downloadFunction = function (response, remoteID) {
	var success = false;
	for (var i = 0; i < userIDs.length; i++) {
		if (userIDs[i].username == remoteID) {
			success = true;
			response.end("1");
			break;
		}
	}
	if (success == false) {
		response.end("0");
	}
};

var deleteFunction = function (targetID) {
	for (var i = 0; i < userIDs.length; i++) {
		if (userIDs[i].username == targetID)
			userIDs.splice(i, 1);
	}
};