# CIS-4515-Lab-04

CIS 4515 Lab

Worksheet 4

Instructions: MapChatApp (Part 1)
Write an application that leverages Android location services as well as a mapping library (Google
Maps or third-party mapping services/libraries such as OpenStreetMap and OSM Droid
[https://github.com/osmdroid/osmdroid]). Your application should do the following:
Design an activity that contains a list (or some other adapter-like view) of users (henceforth referred to
as partners – a partner is simply an app user that isn’t you) retrieved from the API provided in the
appendix below. as well as a prominent map that shows a pin for each user. Your list and map views
should be placed on separate fragments, and follow the usual master-detail pattern (i.e. single pane on
small portrait screens, double pane on large and/or landscape screens). Your activity should also have a
mechanism to allow the user to provide an alphanumeric username.
1. The list of partners should me sorted in the adapter view by distance from the user. To do this,
create a java class that will represent partners, and have that class implement Java’s
Comparable interface. Then simply use one of the predefined collection sorting methods to
sort your partners into a collection, and pass that collection to your adapter to be displayed
2. The partner list and map view should be updated automatically every 30 seconds
3. On startup, the user’s location should be reported to the chat server using the API in the
appendix. It should then be reported automatically every time the user moves 10 or more
meters.
