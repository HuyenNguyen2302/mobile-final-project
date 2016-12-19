# mobile-final-project


#Video Demo
https://www.youtube.com/watch?v=4sjcJyNvNJo

#Overview 
<img src = "resources/images/screenshots/splashScreen.png?raw=true" height = "960" width = "540"></img>

WeRideshare is an android app that provides an easy-to-use, consistent system for commuters looking to carpool. Both drivers and riders will be able to save money by sharing rides. The difference between WeRideshare and other cab services like Uber is it does not require the driver to be a full-time driver. Instead, both riders and drivers will provide information about their current location and destination. If the current locations and destinations of a particular driver and rider are at most 1 mile apart, then WeRideshare matches them together. If there are several potential drivers, riders are provided with a list of drivers whose current location and destination are within 1 mile radius. Then, they can send a carpool request to any drivers on the list. After this, the driver can then accept or decline the request to share the ride. Riders will then be picked up and dropped off along the way. In this way, drivers lower their travel cost, while not having to wait for riders like full-time cab drivers. 



#Purpose and Need 

People who live in big cities have the need to carpool. The reason is traffic can get bad. Plus, it is hard and expensive to find a parking lot. For instance, according to Boston Magazine, “forty-six percent of Boston’s workforce drives to their place of business, either alone or in a carpool. There’s approximately one vehicle per household, despite a high rate of carless households (35.8 percent). And how many parking spots await them? The Globe took a stab at figuring it out in March, and came back with a big, fat, “Who knows?” That doesn’t bode well for the city’s looming parking crisis.” 

![Alt text](resources/images/means to work.PNG?raw=true "Means of Transportation to Work in Boston, MA")
Source:http://www.bostonmagazine.com/news/blog/2015/07/15/why-owning-a-car-in-boston-sucks/

Based on the chart, 41% people drove alone, while only 5% of people carpooled. If the people who drove alone can carpool, the number of vehicles on the street in the morning will decrease by a factor of 2 at the minimum.

WeRideshare aims to solve these problems by matching riders with drivers. The match relies on the proximity: if a rider and a driver live within 1 miles and the destinations are 1 miles apart at most, then WeRideshare will match together. However, one of the differences between WeRideshare and other cab services is that it allows users to schedule rides. This feature is particularly useful for students who has to travel on a weekly basis no matter where they are. Besides reducing the number of vehicles in big cities, this features saves students from booking a vehicle every single day during their internships.

#Design
Here is a diagram that can give you a high level idea about the architecture of WeRideshare:
<img src = "resources/images/general state diagram.png?raw=true"></img>

Login Credentials use Firebase

<img src = "resources/images/screenshots/login.png?raw=true" height = "960" width = "540"></img>
<img src = "resources/images/screenshots/firebaseAuth.png?raw=true"></img>
User rider/driver data are stored in Firebase Realtime Database

<img src = "resources/images/screenshots/realtimeDatabase.png?raw=true"></img>
Push notifications for messages are sent through Firebase Cloud Messaging and Notifications
  The WeRideShareServer folder contains the code for sending downstream messages to clients.
  
User location and Path drawing are using Google Maps Android API and Google Places Web API

<img src = "resources/images/screenshots/locations.png?raw=true" height = "960" width = "540"></img>


#Conclusion
In summary, we were able to implement the following features for WeRideshare:
User authentication
User registration
Offload data to Firebase
Retrieve data from Firebase
Use Google Maps API and Google API Client Libraries to develop map-related functionalities.

However, there are many other useful features that could be developed:  
For drivers: An algorithm to allow multiple riders to be picked up along the path to further minimize travel cost 
For drivers: Path suggestion to maximize the number of riders 
For riders: Provide a map of the driver’s location like Uber 
Fior riders: Allow riders to write reviews for drivers 
Drivers have to upload pictures of themselves, their vehicles, and driver’s license so riders can verify the information. 
Drivers can see a list of potential riders.
Implement horizontal layout 
Remove duplicates for messages: Currently, if user A and user B exchange messages with other, then in user A’s messages, there is a message from user A to user B, and there is also a message from user B to user A. These two messages also appear in user B’s list of messages.
Payment method
Matching algorithm
Drivers can accept request (special case: automatic decline when the vehicle is full)



