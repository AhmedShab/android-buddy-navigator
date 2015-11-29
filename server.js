var http = require("http");
var querystring = require("querystring");

const PORT = 8080;
const SUCCESS = "OK";
const FAILURE = "NO";

/* Jemboy and Obama are 2 built-in user profiles for testing.. this is the format to store the data */
var userIDs = 	[
					{"username" : "Jemboy", "latitude" : 0.0, "longitude" : 5.0},
					{"username" : "Obama", "latitude" : 10.0, "longitude" : 20.0}
				];

var handler = function(request, response) {
	request.on("data", function(data) {
		var post = querystring.parse(data.toString());
		var url = request.url;
		switch(url) {
			case "/":
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				console.log("Welcome"); // ???
				break;

			case "/upload_id":
				/* Check if ID exists.. if it doesn't then push it to userIDs */
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var username = post["username"];
				var available = true;
				for (var i = 0; i < userIDs.length && available; i++) {
					if (userIDs[i].username == username) {
						available = false;
						var jsonString = getSuccessOrFailure(FAILURE);
						response.end(jsonString);
					}
				}
				if (available) {
					var jsonObject = new Object();
					jsonObject["username"] = username;
					jsonObject["latitude"] = 0.0;
					jsonObject["longitude"] = 0.0;
					userIDs.push(jsonObject);
					
					var jsonString = getSuccessOrFailure(SUCCESS);
					response.end(jsonString);
				}
				break;

			case "/download_id":
				/* Check if ID exists.. if it does then it is a successful remote ID acquisition */
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var username = post["username"];
				var available = false;
				for (var i = 0; i < userIDs.length && !available; i++) {
					if (userIDs[i].username == username) {
						available = true;
						var jsonString = getSuccessOrFailure(SUCCESS);
						response.end(jsonString);
					}
				}
				if (!available) {
					var jsonString = getSuccessOrFailure(FAILURE);
					response.end(jsonString);
				}
				break;

			case "/delete_id":
				/* Check if ID exists.. delete it if it does */
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var username = post["username"];
				var deleted = false;
				for (var i = 0; i < userIDs.length && !deleted; i++) {
					if (userIDs[i].username == username) {
						deleted = true;
						userIDs.splice(i, 1);
						var jsonString = getSuccessOrFailure(SUCCESS);
						response.end(jsonString);
					}
				}
				if (!deleted) {
					var jsonString = getSuccessOrFailure(FAILURE);
					response.end(jsonString);
				}
				break;

			case "/upload_coordinates":
				/* Updates coordinates for the given ID */
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var username = post["username"], 
					latitude = parseFloat(post["latitude"]), 
					longitude = parseFloat(post["longitude"]);
				var updated = false;
				for (var i = 0; i < userIDs.length && !updated; i++) {
					if (userIDs[i].username == username) {
						updated = true;
						userIDs[i].latitude = latitude;
						userIDs[i].longitude = longitude;
						var jsonString = getSuccessOrFailure(SUCCESS);
						response.end(jsonString);
						console.log(username + ": " + latitude + ", " + longitude);
					}
				}
				if (!updated) {
					var jsonString = getSuccessOrFailure(FAILURE);
					response.end(jsonString);
				}
				break;

			case "/download_coordinates":
				/* Get the coordinates of the given ID and send it to the client through a JSON string */
				response.writeHead(200, {"Content-Type": "application/x-www-form-urlencoded"});
				var username = post["username"];
				var available = false;
				for (var i = 0; i < userIDs.length && !available; i++) {
					if (userIDs[i].username == username) {
						found = true;
						var latitude = userIDs[i].latitude, longitude = userIDs[i].longitude;
						var jsonString = getSuccessWithCoordinates(latitude, longitude);
						response.end(jsonString);
					}
				}
				if (!available) {
					var jsonString = getSuccessOrFailure(FAILURE);
					response.end(jsonString);
				}
				break;

			default:
				response.writeHead(404, {"Content-Type": "application/x-www-form-urlencoded"});
				console.log("Error"); // ???
		}
	});
};

var server = http.createServer(handler);
server.listen(PORT, function() {
	console.log("Server listening at: http://localhost:" + PORT);
});

var getSuccessOrFailure = function(result) {
	var jsonObject = new Object();
	jsonObject["result"] = result;
	return JSON.stringify(jsonObject);
};

var getSuccessWithCoordinates = function(latitude, longitude) {
	var jsonObject = new Object();
	jsonObject["result"] = SUCCESS;
	jsonObject["latitude"] = latitude;
	jsonObject["longitude"] = longitude;
	return JSON.stringify(jsonObject);
};