package com.example.inclass01;


import android.content.Context;
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

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;


public class ChatRoomMain extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    OnFragmentInteractionListener mListener;
    String TAG = "demo";

    public ChatRoomMain() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom_main, container, false);

        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.displayRoomsRecyclerView);
        recyclerView.setHasFixedSize(true);

        setHasOptionsMenu(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        ArrayList<ChatRoom> roomList = mListener.getChatRoomList();
        Log.d(TAG, "onCreateView: " + roomList.toString());
        mAdapter = new ChatRoomMainAdapter(roomList, (Context) mListener);
        recyclerView.setAdapter(mAdapter);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.custom_menu, menu);

        menu.findItem(R.id.logout).setVisible(true);
        menu.findItem(R.id.addChatroom).setVisible(true);
        menu.findItem(R.id.onlineUsers).setVisible(false);
        menu.findItem(R.id.myAccount).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addChatroom:
                mListener.addChatroom();
                return true;

            case R.id.logout:
                mListener.logOut();
                return true;

            case R.id.myAccount:
                mListener.myAccount();
                return true;
            case R.id.allUsers:
                mListener.displayAllUsers();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public interface OnFragmentInteractionListener {
        ArrayList<ChatRoom> getChatRoomList();

        void addChatroom();

        void myAccount();

        void logOut();

        void displayAllUsers();
    }
}
