# CIS-4515-Lab05

CIS 4515

Worksheet 5

Instructions: MapChatApp (Part 2)
Combining components from your previous labs, extend you most recent lab to facilitate end-to-end
encrypted chat between a user and a partner. The chat component will be built on Google’s FCM
service.
You application will allow a user to receive and keep track of multiple public keys, and associate those
keys with a username. See the format in the appendix below. Do not worry about updating
keys/usernames in the event of changes; if a partner changes their username, just treat them as a new
partner. Note that if you change your username, you will invalidate any previously exchanged public
keys since partners will not be able to match your username to a public key and you will need to repeat
that exercise.
Because of a technical detail that will be explained later, please refactor your applications package
name/id to be edu.temple.mapchat
To integrate Firebase into your application you can use Android Studio’s built in wizard under Tools →
Firebase → Cloud Messaging, or manually though the Firebase Console
(https://console.firebase.google.com).
Once integrated, test your application by closing all activities (after running it at least once after
integrating Firebase) and sending a notification to your app (or for simplicity, to all instances of your
app) through the Firebase Console. You should receive a notification on your device and clicking it
should launch your app.
Once you have implemented and verified that Firebase messaging is functional, incorporate the
following specifications into your app:
1. Your application’s “main screen” should display a map of chat users, as well as a list of sorted
chat partners (your previous lab).
2. When a clicks on one a chat partner in the list view, one of 2 actions should take place:
1. The user will be taken to a chat interface (this can be a new Activity) where they can
begin sending messages to each other. This happens if the user had previously received the
partner’s public key.
or
2. The user is shown an error message that they are unable to chat. This happens if the user
had not previously received the partner’s public key. You will need to meet with this partner in
person to exchange keys. This is referred to as an out-of-band key exchange, and ensures that
you can trust the person you’re speaking with over the network.
3. From the chat screen, the user should be able to type a message to send to the partner (Limit
chat messages to no more than 160 characters to ensure compatibility with the asymmetric
encryption mechanism we had previously used). When the message is typed and the user clicks
Send, the message should be encrypted using the partner’s public key and then sent to the chat
server using the API detailed in the appendix. The typed message should be shown in the
message display area with right justification.
A simple implementation of a message display is to use a ListView whose adapter will generate a (fairly)
simple layout containing a few textviews showing the sender name and message content. Place the
listview inside a ScrollView object. Whenever a new message is sent or received, add the message object
to the collection used by the listview’s adapter, and then notify the adapter that the dataset has changed
by calling its notifyDataSetChaged() method.
You may then want to autoscroll to the bottom of the listview to ensure you’re displaying the new
message by doing something like:
listView.smoothScrollToPosition(messageCollection.size() - 1);
1. On the recipient's end, messages will be received through FCM. For each received message,
display both the message and sender in the message display area with left justification. To
facilitate passing a message received by a FirebaseMessagingService to the activity, you may
want to use a Broadcast and Broadcast Receiver – investigate implementing a broadcast
receiver within an activity (as opposed to creating a public broadcast receiver in its own file).
You have two weeks to complete this assignment. Upload your project or a GitHub link to
Canvas, or make arrangements to show your work to the TA BEFORE the beginning of the
next lab.
NOTES
For your chat activity, you can disable rotation, etc. to reduce the opportunities for the activity to be
destroyed and recreated. In practice however, you would want to keep a log file of current messages,
and subsequently read the messages from the log file and redisplay them if the activity is recreated.
If you would like to use actual chat bubble images similar to those you see in popular instant messaging
applications you can use 9-patch images (Info: https://developer.android.com/guide/topics/graphics/2dgraphics.html#nine-patch,
Guide: https://developer.android.com/studio/write/draw9patch.html)
Appendix
Data formats
1) Key exchange format uses a JSON object with the following format:
{"user":"username","key":"PEM data"}
e.g.
{"username":"karlmorris","key":"-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAryQICCl6NZ5gDKrnSztO
3Hy8PEUcuyvg/ikC+VcIo2SFFSf18a3IMYldIugqqqZCs4/4uVW3sbdLs/6PfgdX
7O9D22ZiFWHPYA2k2N744MNiCD1UE+tJyllUhSblK48bn+v1oZHCM0nYQ2NqUkvS
j+hwUU3RiWl7x3D2s9wSdNt7XUtW05a/FXehsPSiJfKvHJJnGOX0BgTvkLnkAOTd
OrUZ/wK69Dzu4IvrN4vs9Nes8vbwPa/ddZEzGR0cQMt0JBkhk9kU/qwqUseP1QRJ
5I1jR4g8aYPL/ke9K35PxZWuDp3U0UPAZ3PjFAh+5T+fc7gzCs9dPzSHloruU+gl
FQIDAQAB-----END PUBLIC KEY-----"}
2) Chat message format uses a JSON object with the following format:
{"to":"partnername","from":"username","message":"ENCRYPTED MESSAGE"}
e.g.
{"to":"martin","from":"karlmorris","message":"%^FDVHRTY*EW$(NGDFSGDHg"}
When a chat message is received by a user, the payload will be the exact message as above.
Firebase information
1) To ensure that apps created by different programmers can exchange messages, we will all use the
same Google Project and identifier (already created). If/when you have tested your app using your own
Google project, replace your google-services.json file with the one found here:
https://kamorris.com/temple/fcmhelper/google-services.json
2) The String representing the JSON message object will be retrievable in the onMessgeReceived()
method of your FirebaseMessagingSerivce class using:
remoteMessage.getData().get("payload")
App APIs
1) To register your FCM token:
POST request to: https://kamorris.com/lab/fcm_register.php
With data: user=username, token=fcm token
Response: OK on success, ERROR on error
2) Send a message to another user:
POST request to: https://kamorris.com/lab/send_message.php
With data: user=username, partneruser=partner username, message=encrypted message
Response: OK on success, ERROR on error
