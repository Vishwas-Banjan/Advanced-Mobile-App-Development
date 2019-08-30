package com.example.inclass01;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class InsideChatroom extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private OnFragmentInteractionListener mListener;
    ChatRoom chatRoom;
    String TAG = "demo";
    EditText editTextMessage;
    ImageView sendButton;

    public InsideChatroom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inside_chatroom, container, false);

        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.displayChatRecyclerView);
        recyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new InsideChatroomAdapter(mListener.getChatList(), (Context) mListener);
        recyclerView.setAdapter(mAdapter);
        mListener.getAdapter(mAdapter);

        editTextMessage = view.findViewById(R.id.sendMessageEditText);
        sendButton = view.findViewById(R.id.sendBtnImageView);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextMessage.getText().toString().trim().equals("")) {
                    Message message = new Message();
                    message.messageText = editTextMessage.getText().toString();
                    message.userId = mListener.getUserId();
                    message.userName = mListener.getUserName();
                    Date currentTime = Calendar.getInstance().getTime();
                    message.messageTimeStamp = String.valueOf(currentTime);
                    message.chatRoomId = chatRoom.roomId;
                    mListener.sendMessage(message, editTextMessage);
                }
            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.custom_menu, menu);
        menu.findItem(R.id.logout).setVisible(false);
        menu.findItem(R.id.addChatroom).setVisible(false);
        menu.findItem(R.id.onlineUsers).setVisible(true);
        menu.findItem(R.id.myAccount).setVisible(false);
        menu.findItem(R.id.allUsers).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.onlineUsers:
                mListener.getOnlineUsers();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public interface OnFragmentInteractionListener {
        ArrayList<Message> getChatList();

        void getAdapter(RecyclerView.Adapter mAdapter);

        String getUserId();

        String getUserName();

        void getOnlineUsers();

        void sendMessage(Message message, EditText editTextMessage);

    }
}
