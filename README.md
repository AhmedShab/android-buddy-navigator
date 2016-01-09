## Buddy Navigator

Buddy Navigator is a compass for your buddies. It is a cursor that points in your friend's direction. It makes use of the Google API and requires both GPS and Internet connection to work.

I implemented the [Google API](https://developers.google.com/android/guides/api-client) to get the latitude and longitude of the device in a given time interval.

## Usage

* Download NodeJS [here](https://nodejs.org/en/download/)
* Open the terminal and execute the server file with the command ```node server```
* Change the server IP in the Constants class to your IPv4 address
* Run the application.
* Enter the usernames of yourself and your buddy's
* Walk through walls until you reach your friend!

## Important Notes

You need to be connected to the same network so both devices can access the NodeJS server. I worked on this project in my school campus so I could test it throughout the whole area. 

Also note that GPS does not work very well indoors! It may not be accurate at times so if you test this application inside a building it could point out in the wrong direction. you Be sure to read on [indoor positioning system](https://en.wikipedia.org/wiki/Indoor_positioning_system). IPS is not standardized yet so I had to work with GPS.

## TODO

I still need to implement a way to figure out if the phone is not being held straight and facing north. If the device and the holder is facing in the direction of the north the cursor will be pointing correctly. In other cases it does not.

## License

Licensed under the incredibly [permissive](http://en.wikipedia.org/wiki/Permissive_free_software_licence) [MIT license](http://creativecommons.org/licenses/MIT/)