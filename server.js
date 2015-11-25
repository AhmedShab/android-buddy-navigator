var http = require("http");
var querystring = require("querystring");

const PORT = 8080;

/* Jemboy and Obama are 2 built-in user profiles for testing.. this is the format to store the data */
var userIDs = 	[
					{"username" : "Jemboy", "latitude" : 0.0, "longitude" : 0.0},
					{"username" : "Obama", "latitude" : 10.0, "longitude" : 10.0}
				];


var handler = function(request, response) {
	request.on("data", function(data) {
		var post = querystring.parse(data.toString());
		var url = request.url;
		switch(url) {
			case "/":
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				console.log("Welcome."); // ???
				break;

			case "/upload_id":
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var id = post["id"];
				upload_id(response, id);
				break;

			case "/download_id":
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var id = post["id"];
				download_id(response, id);
				break;

			case "/delete_id":
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var id = post["id"];
				delete_id(response, id);
				break;

			case "/upload_coordinates":
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var id = post["id"], latitude = post["latitude"], longitude = post["longitude"];
				upload_coordinates(response, id, latitude, longitude);
				break;

			case "/download_coordinates":
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var id = post["id"];
				download_coordinates(response, id);
				break;

			default:
				console.log("Page Not Found."); // ???
				response.writeHead(404, {"Content-Type": "application/x-www-form-urlencoded"});
		}
	});
};

var server = http.createServer(handler);
server.listen(PORT, function() {
	console.log("Server listening at: http://localhost:" + PORT);
});

var upload_id = function(response, id) {
	/* Check if ID exists.. if it doesn't then push it to userIDs */
	var success = true;
	for (var i = 0; i < userIDs.length && success; i++) {
		if (userIDs[i].username == localID) {
			success = false;
			var result = getJSONResult("Failure");
			response.end(JSON.stringify(jsonObject));
		}
	}
	if (success) {
		var jsonObject = new Object();
		jsonObject["username"] = localID;
		jsonObject["latitude"] = 0.0;
		jsonObject["longitude"] = 0.0;
		userIDs.push(jsonObject);
		var result = getJSONResult("Success");
		response.end(result);
	}
};

var download_id = function(response, id) {
	/* Check if ID exists.. if it does then it is a successful remote ID acquisition */
	var success = false;
	for (var i = 0; i < userIDs.length && !success; i++) {
		if (userIDs[i].username == remoteID) {
			success = true;
			var result = getJSONResult("Success");
			response.end(result);
		}
	}
	if (success == false) {
		var result = getJSONResult("Failure");
		response.end(result);
	}
};

var delete_id = function(response, id) {
	var found = false;
	for (var i = 0; i < userIDs.length && !found; i++) {
		if (userIDs[i].username == targetID) {
			found = true;
			userIDs.splice(i, 1);
			var result = getJSONResult("Success");
			response.end(result);
		}
	}
	if (!found) {
		var result = getJSONResult("Failure");
		response.end(result);
	}
};

var upload_coordinates = function(response, id, latitude, longitude) {
	/* Updates coordinates for the given ID */
	var updated = false;
	for (var i = 0; i < userIDs.length && !updated; i++) {
		if (userIDs[i].username == localID) {
			userIDs[i].latitude = latitude;
			userIDs[i].longitude = longitude;
			var result = getJSONResult("Success");
			response.end(result);
			updated = true;
		}
	}
	if (!updated) {
		var result = getJSONResult("Failure");
		response.end(result);
	}
};

var download_coordinates = function(response, id) {
	/* Get the coordinates of the given ID and send it to the client through a JSON string */
	var found = false;
	for (var i = 0; i < userIDs.length && !found; i++) {
		if (userIDs[i].username = remoteID) {
			var jsonObject = new Object();
			jsonObject["result"] = "Success"
			jsonObject["latitude"] = userIDs[i].latitude;
			jsonObject["longitude"] = userIDs[i].longitude;
			response.end(JSON.stringify(jsonObject));
			found = true;
		}
	}
	if (!found) {
		var result = getJSONResult("Failure");
		response.end(result);
	}
};

var getJSONResult = function (result) {
	var jsonObject = new Object();
	jsonObject["result"] = result;
	return JSON.stringify(jsonObject);
};