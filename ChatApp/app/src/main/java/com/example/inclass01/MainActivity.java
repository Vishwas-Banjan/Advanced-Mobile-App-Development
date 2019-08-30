package com.example.inclass01;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ChatRoomMain.OnFragmentInteractionListener,
        ChatRoomMainAdapter.OnAdapterInteractionListener, InsideChatroom.OnFragmentInteractionListener,
        InsideChatroomAdapter.OnAdapterInteractionListener, LogInFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener, ResetPasswordFragment.OnFragmentInteractionListener,
        DisplayMyAccount.OnFragmentInteractionListener, EditUserProfile.OnFragmentInteractionListener {

    String TAG = "demo";
    ArrayList<ChatRoom> chatRoomsList = new ArrayList<>();
    ArrayList<Message> messageArrayList = new ArrayList<>();
    ArrayList<String> onlineUsers = new ArrayList<>();
    ArrayList<String> onlineUserIds = new ArrayList<>();
    ArrayList<String> allUserNames = new ArrayList<>();
    ArrayList<String> allUserNIds = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("ChatApp");
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
        displayLoginFragment();
    }


    public void displayChatRoomList() {
        setTitle("Chatrooms");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ChatRoomMain(), "ChatRoomMain")
                .commit();
    }

    String currentChatRoomId;

    public void displayChatRoom(ChatRoom chatRoom) {
        setTitle(chatRoom.roomName);
        currentChatRoomId = chatRoom.roomId;
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("displayChatRooms")
                .replace(R.id.container, new InsideChatroom(chatRoom), "InsideChatRoom")
                .commit();
    }

    public void displayLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LogInFragment(), "LogInFragment")
                .commit();
    }

    @Override
    public ArrayList<ChatRoom> getChatRoomList() {
        return chatRoomsList;
    }

    @Override
    public void addChatroom() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Chatroom Name");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: " + input.getText().toString());

                Map<String, Object> data = new HashMap<>();
                data.put("chatRoomName", input.getText().toString());
                db.collection("Chatrooms").add(data)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void myAccount() {
        DocumentReference docRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = new User();
                        user.userId = document.getId();
                        user.firstName = (String) document.getData().get("userFirstName");
                        user.lastName = (String) document.getData().get("userLastName");
                        user.userEmail = (String) document.getData().get("userEmail");
                        user.gender = (String) document.getData().get("userGender");
                        user.city = (String) document.getData().get("userCity");
                        user.profileImage = (String) document.getData().get("userProfileImage");
                        displayAccount(user);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void displayAccount(User user) {
        setTitle("Account Details");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new DisplayMyAccount(user), "DisplayAccount")
                .commit();
    }

    @Override
    public void logOut() {
        FirebaseAuth.getInstance().signOut();
        loggedInUserId = "";
        loggedInUserName = "";
        messageArrayList.clear();
        chatRoomsList.clear();
        onlineUsers.clear();
        onlineUserIds.clear();
        allUserNames.clear();
        allUserNIds.clear();

        logInFragment();
    }


    @Override
    public void displayAllUsers() {
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allUserNIds.clear();
                            allUserNames.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allUserNames.add(document.getData().get("userFirstName") + " "
                                        + document.getData().get("userLastName"));
                                allUserNIds.add(document.getId());
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            if (!allUserNames.isEmpty()) {
                                builder.setTitle("All Users: ")
                                        .setItems(allUserNames.toArray(new CharSequence[allUserNames.size()]), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DocumentReference docRef = db.collection("Users").document(allUserNIds.get(which));
                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                User user = new User();
                                                                user.userId = document.getId();
                                                                user.firstName = (String) document.getData().get("userFirstName");
                                                                user.lastName = (String) document.getData().get("userLastName");
                                                                user.userEmail = (String) document.getData().get("userEmail");
                                                                user.gender = (String) document.getData().get("userGender");
                                                                user.city = (String) document.getData().get("userCity");
                                                                user.profileImage = (String) document.getData().get("userProfileImage");
                                                                displayAccount(user);
                                                            } else {
                                                                Log.d(TAG, "No such document");
                                                            }
                                                        } else {
                                                            Log.d(TAG, "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    public void enterChatRoom(final ChatRoom chatRoom) {
        displayChatRoom(chatRoom);

        Map<String, Object> data = new HashMap<>();
        data.put("isOnline", true);
        data.put("userName", mAuth.getCurrentUser().getDisplayName());
        DocumentReference userReference = db.collection("Chatrooms").document(chatRoom.roomId).collection("Users").document(loggedInUserId);
        userReference.set(data, SetOptions.merge());

        CollectionReference onlineRef = db.collection("Chatrooms").document(chatRoom.roomId).collection("Users");
        onlineRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                onlineUsers.clear();
                onlineUserIds.clear();
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    if ((boolean) queryDocumentSnapshot.getData().get("isOnline") &&
                            queryDocumentSnapshot.getData().get("isOnline") != "null") {
                        Log.d(TAG, "onEvent: " + (String) queryDocumentSnapshot.getData().get("userName"));
                        onlineUsers.add((String) queryDocumentSnapshot.getData().get("userName"));
                        onlineUserIds.add(queryDocumentSnapshot.getId());
                    }
                }
            }
        });

        CollectionReference messagesRef = db.collection("Messages");
        messagesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                messageArrayList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.getData().get("chatRoomId").equals(chatRoom.roomId)) {
                        Message message = new Message();
                        message.userName = (String) doc.get("userName");
                        message.userId = (String) doc.get("userId");
                        message.messageId = doc.getId();
                        message.messageText = (String) doc.get("messageText");
                        message.chatRoomId = (String) doc.get("chatRoomId");
                        if (doc.getData().get("messagesUpvotedBy") != null)
                            message.upvotedBy = (ArrayList<String>) doc.getData().get("messagesUpvotedBy");
                        else message.upvotedBy = new ArrayList<>();
                        message.messageTimeStamp = doc.get("messageTime") + "";
                        messageArrayList.add(message);
                        messageArrayList.sort(new Comparator<Message>() {
                            DateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

                            @Override
                            public int compare(Message o1, Message o2) {
                                try {
                                    return f.parse(o1.messageTimeStamp).compareTo(f.parse(o2.messageTimeStamp));
                                } catch (ParseException e) {
                                    throw new IllegalArgumentException(e);
                                }
                            }
                        });
                        generateChatsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public ArrayList<Message> getChatList() {
        return messageArrayList;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Close the App?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Go to Chatroom List", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            displayChatRoomList();
                            if (currentChatRoomId != null) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("isOnline", false);
                                DocumentReference userReference = db.collection("Chatrooms").document(currentChatRoomId).collection("Users").document(loggedInUserId);
                                userReference.set(data, SetOptions.merge());
                            }
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            displayChatRoomList();
                            Map<String, Object> data = new HashMap<>();
                            data.put("isOnline", false);
                            DocumentReference userReference = db.collection("Chatrooms").document(currentChatRoomId).collection("Users").document(loggedInUserId);
                            userReference.set(data, SetOptions.merge());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    RecyclerView.Adapter generateChatsAdapter;

    @Override
    public void getAdapter(RecyclerView.Adapter mAdapter) {
        generateChatsAdapter = mAdapter;
    }

    @Override
    public void sendMessage(final Message message, final EditText editTextMessage) {
        final ArrayList<String> userIdList = new ArrayList<>();
        CollectionReference docRef = db.collection("Chatrooms").document(message.chatRoomId).collection("Users");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userIdList.add(document.getId());
                            }
                            if (userIdList.contains(message.userId)) {
                                CollectionReference messageRef = db.collection("Messages");
                                Map<String, Object> messageMap = new HashMap<>();
                                messageMap.put("messageText", message.messageText);
                                messageMap.put("messageTime", message.messageTimeStamp);
                                messageMap.put("userId", message.userId);
                                messageMap.put("userName", message.userName);
                                messageMap.put("chatRoomId", message.chatRoomId);
                                messageRef
                                        .add(messageMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                DocumentReference docRef = db.collection("Chatrooms").document(message.chatRoomId).collection("Users").document(loggedInUserId);
                                                docRef.update("messages", FieldValue.arrayUnion(documentReference.getId()));
                                                editTextMessage.setText("");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
                            } else {
                                Log.d(TAG, "onComplete: User Not Present");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public String getUserId() {
        return loggedInUserId;
    }

    @Override
    public void getMessageProfileImage(final ImageView imageView, String userId) {
        DocumentReference userRef = db.collection("Users").document(userId);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (document.get("userProfileImage") != null) {
                            Picasso.get().load((String) document.getData().get("userProfileImage")).into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Log.d("demo", "onSuccess: Success!");
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    @Override
    public String getUserName() {
        return loggedInUserName;
    }

    @Override
    public void getOnlineUsers() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        if (!onlineUsers.isEmpty()) {
            builder.setTitle("Online Users: ")
                    .setItems(onlineUsers.toArray(new CharSequence[onlineUsers.size()]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DocumentReference docRef = db.collection("Users").document(onlineUserIds.get(which));
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            User user = new User();
                                            user.userId = document.getId();
                                            user.firstName = (String) document.getData().get("userFirstName");
                                            user.lastName = (String) document.getData().get("userLastName");
                                            user.userEmail = (String) document.getData().get("userEmail");
                                            user.gender = (String) document.getData().get("userGender");
                                            user.city = (String) document.getData().get("userCity");
                                            user.profileImage = (String) document.getData().get("userProfileImage");
                                            displayAccount(user);
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    String loggedInUserId = "";
    String loggedInUserName = "";

    void setLoggedInUser(FirebaseUser setUser) {
        loggedInUserId = setUser.getUid();
        loggedInUserName = setUser.getDisplayName();

        final CollectionReference docRef = db.collection("Chatrooms");
        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                chatRoomsList.clear();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    if (document.getData() != null) {
                        ChatRoom room = new ChatRoom((String) document.getData().get("chatRoomName"), document.getId());
                        chatRoomsList.add(room);
                        chatRoomsList.sort(new Comparator<ChatRoom>() {
                            @Override
                            public int compare(ChatRoom o1, ChatRoom o2) {
                                return o1.roomName.compareTo(o2.roomName);
                            }
                        });
                    }
                }
                displayChatRoomList();
            }
        });
    }

    @Override
    public void deleteClicked(Message message) {
        Log.d(TAG, "onClick: Delete Clicked");
        db.collection("Messages").document(message.messageId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        DocumentReference docRef = db.collection("Chatrooms").document(message.chatRoomId).collection("Users").document(loggedInUserId);
        docRef.update("messages", FieldValue.arrayRemove(message.messageId)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Message successfully deleted!");
            }
        });

    }

    @Override
    public void upvoteClicked(Message message) {
        Log.d(TAG, "onClick: UpVote Clicked MessageID: " + message.chatRoomId + "User ID: " + loggedInUserId);

        DocumentReference docRef = db.collection("Chatrooms").document(message.chatRoomId).collection("Users").document(loggedInUserId);
        docRef.update("messagesUpvoted", FieldValue.arrayUnion(message.messageId));

        DocumentReference messageRef = db.collection("Messages").document(message.messageId);
        messageRef.update("messagesUpvotedBy", FieldValue.arrayUnion(loggedInUserId));
    }

    @Override
    public void logIn(User user) {
        mAuth.signInWithEmailAndPassword(user.userEmail, user.userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Log In Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser LogInUser = mAuth.getCurrentUser();
                            setLoggedInUser(LogInUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure ", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void SignUpFragment() {
        setTitle("Sign Up");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignUpFragment(), "SignUpFragment")
                .commit();
    }

    @Override
    public void ResetFragment() {
        setTitle("Reset Password");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ResetPasswordFragment(), "resetPasswordFragment")
                .commit();
    }

    @Override
    public void signUp(final User user) {

        mAuth.createUserWithEmailAndPassword(user.userEmail, user.userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser SignUpUser = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.firstName + " " + user.lastName)
                                    .build();
                            SignUpUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                Toast.makeText(MainActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                                createUserDB(user);
                                                setLoggedInUser(SignUpUser);
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure ", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed. " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createUserDB(final User user) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (cameraBitmap != null || galleryBitmap != null) {
            Bitmap bitmap = (cameraBitmap != null) ? cameraBitmap : galleryBitmap;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            String path = "UserProfile/" + mAuth.getCurrentUser().getUid() + ".png";
            final StorageReference imageRef = mStorage.getReference(path);
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("ID", mAuth.getCurrentUser().getUid())
                    .build();
            UploadTask uploadTask = imageRef.putBytes(data, metadata);
            uploadTask.addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    mStorage.getReference().child("/UserProfile/" + mAuth.getCurrentUser().getUid() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("userFirstName", user.firstName);
                            data.put("userLastName", user.lastName);
                            data.put("userEmail", user.userEmail);
                            data.put("userProfileImage", uri.toString());
                            data.put("userGender", user.gender);
                            data.put("userCity", user.city);
                            db.collection("Users").document(mAuth.getCurrentUser().getUid())
                                    .set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                            cameraBitmap = null;
                            galleryBitmap = null;
                        }
                    });
                }
            });
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("userFirstName", user.firstName);
            data.put("userLastName", user.lastName);
            data.put("userEmail", user.userEmail);
            data.put("userGender", user.gender);
            data.put("userCity", user.city);
            data.put("userProfileImage", user.profileImage);

            db.collection("Users").document(mAuth.getCurrentUser().getUid())
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }

    ImageView userProfileImage;
    String[] chooseCameraOption = {"Take from Camera", "Choose from Gallery"};

    @Override
    public void selectImage(ImageView imageUpload) {
        userProfileImage = imageUpload;
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose Option");
        builder.setItems(chooseCameraOption, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    dispatchTakePictureIntent();
                } else {
                    dispatchSelectPictureIntent();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void updateUser(final User user) {
        final FirebaseUser editUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.firstName + " " + user.lastName)
                .build();


        editUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(MainActivity.this, "Edit Update Successful", Toast.LENGTH_SHORT).show();
                            createUserDB(user);
                        }
                    }
                });


        createUserDB(user);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_FROM_GALLERY = 2;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchSelectPictureIntent() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickPictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPictureIntent, REQUEST_IMAGE_FROM_GALLERY);
        }
    }

    Bitmap galleryBitmap = null;
    Bitmap cameraBitmap = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            cameraBitmap = (Bitmap) extras.get("data");
            cameraBitmap = Bitmap.createScaledBitmap(cameraBitmap, 600, 600, true);
            userProfileImage.setImageBitmap(cameraBitmap);
        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                galleryBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                galleryBitmap = Bitmap.createScaledBitmap(galleryBitmap, 600, 600, true);
                userProfileImage.setImageBitmap(galleryBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void resetEmail(User user) {
        mAuth.sendPasswordResetEmail(user.userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent." + task.toString());
                            logInFragment();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void logInFragment() {
        setTitle("Login");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LogInFragment(), "LogInFragment")
                .commit();
    }
}
