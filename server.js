var http = require("http");
var querystring = require("querystring");

const PORT = 8080;

/* Jemboy and Obama are 2 built-in user profiles for testing.. this is the format to store the data */
var userIDs = 	[
					{"username" : "Jemboy", "latitude" : 0.0, "longitude" : 0.0},
					{"username" : "Obama", "latitude" : 10.0, "longitude" : 10.0}
				];

var server = http.createServer(function (request, response) {
	response.writeHead(200, "Header", {"Content-Type": "application/x-www-form-urlencoded"});

	/* Data is always of the following form: key=value&key2=value2.. values are accessed through post[key] */
	request.on("data", function (data) {
		var post = querystring.parse(data.toString());
		var request = post["request"];
		
		/* request=uploadID&localID=\someuserinput\ */
		if (request == "uploadID") {
			var localID = post["localID"];
			uploadFunction(response, localID);
		}

		/* request=downloadID&remoteID=\someuserinput\ */
		if (request == "downloadID") {
			var remoteID = post["remoteID"];
			downloadFunction(response, remoteID);
		}

		/* request=deleteID&targetID=\pastuserinput\ -- deleteID is called when the user already has an ID but he changes it */
		if (request == "deleteID") {
			var targetID = post["targetID"];
			deleteFunction(response, targetID);
		}

		/* request=uploadCoord&localID=\somevariable\&latitude=\somevariable\&longitude=\somevariable\ */
		if (request == "uploadCoord") {
			var localID = post["localID"], latitude = post["latitude"], longitude = post["longitude"];
			uploadCoord(response, localID, latitude, longitude);
		}

		/* request=downloadCoord&remoteID=\somevariable\ */
		if (request == "downloadCoord") {
			var remoteID = post["remoteID"];
			downloadCoord(response, remoteID);
		}
		console.log(userIDs);
	});
});

server.listen(PORT, function () {
	console.log("Server listening at: http://localhost:" + PORT);
});

var uploadFunction = function (response, localID) {
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

var downloadFunction = function (response, remoteID) {
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

var deleteFunction = function (response, targetID) {
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

var uploadCoord = function (response, localID, latitude, longitude) {
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

var downloadCoord = function (response, remoteID) {
	/* Get the coordinates of the given ID and send it to the client through a JSON string */
	var found = false;
	for (var i = 0; i < userIDs.length && !found; i++) {
		if (userIDs[i].username = remoteID) {
			var jsonObject = new Object();
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
