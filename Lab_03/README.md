# CIS-4515-Lab-03

CIS 4515

Worksheet 3

Instructions:
We are going to build a trivial encrypted message exchange app that will extend your certificate
provider application (built around the content provider). We will add a feature to transfer and receive
public keys from external instances of your application using Android Beam (NFC).
1. Familiarize yourself with the PEM file format (https://en.wikipedia.org/wiki/Privacyenhanced_
Electronic_Mail) and implementing it in Android (http://bfy.tw/AV90)
2. Implement a key transfer function based on Android Beam
3. When a user (User A) creates their public-private key pair, they should be able to transfer their
public key to a third party running the same application (User B).
4. Once the key is transferred, User A should now be able to type and then encrypt some text using
their private key (similar to the previous lab), and thereafter transfer the encrypted text to User
B also using Android Beam
5. Once received, User B should automatically decrypt the received text using User A’s public key
and display it.
6. NOTES
For this application, we’ll only be exchanging messages with a single external user at a time so
there’s no need to manage an arbitrary amount of public keys. Whenever we encounter a new
user (User C) and receive their public key, we can overwrite User B’s public key.
If you don’t have an additional Android device with NFC capabilities, you are free to work with
your group members in order to properly test your app, but you must still do your own work.
