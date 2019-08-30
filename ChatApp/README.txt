File : google-services.json file not included!

Here, Created a simple chat application using Firebase.

App Screens and Understanding:

Login Page : App provides Login authentication using the Firebase email/password authentication.

Reset Password Page : In addition also implement the forgot password feature by using the Firebase features, which sends an email to the registered email ID with reset link.

Sign Up Page : App provides Signup using Firebase, Storing User data (First Name, Last Name, Email, City, Gender) in Cloud Firestore and Profile Image in Firebase Storage.

Chatroom List Page :

This page lists all the chatrooms that have been created. There are two menu buttons on the top right. One allows users to create a chatroom of their own name, other allows user to Log Out. More Option button, consists of button which is a list of users in the system, clicking on that user will lead to that user's profile. Also consists of button "My Account" which shows and allows logged in User's to view and edit their Profile Information.

Inside Chatroom
This page show list of all the messages in order of time, It also has an option to view Online Users, which shows all the users online in that particular chatroom. Messages sent by the logged in user in a particular chat room can be deleted using the delete button next to the button. User can also upvote/like a message from other user, below the button it shows a count of likes/upvotes received to that message.

Database Design: Here, we chose to use Cloud Firestore for the simplicity of working with complex database structure, ability to create documents and sub-collection within a collection.

There are basically 3 Collections.

Chatrooms: Consists of list of unique id generated documents, each with field as chatroom name. This document also consists a Collection - Users, which keeps track of users online in that particular chatroom. In Users Collection, There are documents of User with their unique id, fields are :
isOnline:(true/false), messages (array of messages by the particular user), messagesUpvoted (array of messages upvoted by that user), userName (Display name of user)

When a user enters a chatroom, isOnline field is changed to true. If user writes a message in the chatroom, message id is added to the array of messages. If user upvotes a message, message id is added to array of messagesUpvoted. If user deletes his message, that particular message id is removed from message array.

Messages: Consists of unique id generated documents, having fields :
chatRoomId: unique id of chatroom where this message was created. messageText: data of message messageTime: time of message creation messagesUpvotedBy: array of user id's who have upvoted this message userId: user id of user who wrote this message userName: user Name of user who wrote this message

Users: Consists of documents with Id's received from Firebase Authentication while signing up.
Fields consits of :

userCity: User City userEmail: User Email userFirstName: User first Name userLastName: User Last Name userGender: User selected gender userProfileImage: Public downloadable link of image from Firebase Storage
Storage:

Firebase Storage allows file uploads and downloads, regardless of network quality. It allows to store images, audio, video, or other user-generated content. We are using to store User Profile Image.