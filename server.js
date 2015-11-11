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
			deleteFunction(targetID);
		}

		/* request=uploadCoord&localID=\somevariable\&latitude=\somevariable\&longitude=\somevariable\ */
		if (request == "uploadCoord") {
			var localID = post["localID"], latitude = post["latitude"], longitude = post["longitude"];
			uploadCoord(localID, latitude, longitude);
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
	/* Check if ID exists.. if it does then it is a successful remote ID acquisition */
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

var uploadCoord = function (localID, latitude, longitude) {
	/* Updates coordinates for the given ID */
	for (var i = 0; i < userIDs.length; i++) {
		if (userIDs[i].username == localID) {
			userIDs[i].latitude = latitude;
			userIDs[i].longitude = longitude;
			break;
		}
	}
};

var downloadCoord = function (response, remoteID) {
	/* Get the coordinates of the given ID and send it to the client through a JSON string */
	for (var i = 0; i < userIDs.length; i++) {
		if (userIDs[i].username = remoteID) {
			var jsonObject = new Object();
			jsonObject["latitude"] = userIDs[i].latitude;
			jsonObject["longitude"] = userIDs[i].longitude;
			response.end(JSON.stringify(jsonObject));
			break;
		}
	}
};