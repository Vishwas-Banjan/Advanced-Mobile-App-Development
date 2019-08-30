package com.example.inclass01;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatRoomMainAdapter extends RecyclerView.Adapter<ChatRoomMainAdapter.ViewHolder> {
    String TAG = "demo";
    ArrayList<ChatRoom> roomsList;
    OnAdapterInteractionListener mListener;

    public ChatRoomMainAdapter(ArrayList<ChatRoom> roomsList, Context context) {
        this.roomsList = roomsList;
        this.mListener = (OnAdapterInteractionListener) context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_displayrooms, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String name = roomsList.get(position).roomName;
        holder.roomName.setText(name);

        holder.roomName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.enterChatRoom(roomsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.chatRoomNameTextView);
        }
    }

    public interface OnAdapterInteractionListener {
        void enterChatRoom(ChatRoom chatRoom);
    }
}
