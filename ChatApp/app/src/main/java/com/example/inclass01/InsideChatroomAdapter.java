package com.example.inclass01;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InsideChatroomAdapter extends RecyclerView.Adapter<InsideChatroomAdapter.ViewHolder> {

    ArrayList<Message> messages;
    OnAdapterInteractionListener mListener;
    String TAG = "demo";

    public InsideChatroomAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.mListener = (OnAdapterInteractionListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_displaychat, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Message message = messages.get(position);
        holder.userName.setText(message.userName);
        holder.messageText.setText(message.messageText);
        holder.messageTime.setText(String.valueOf(message.messageTimeStamp));

        mListener.getMessageProfileImage(holder.userProfileImage, message.userId);

        holder.upvoteCount.setText(String.valueOf(message.upvotedBy.size()));

        Date out = null;
        try {
//            DateTime Format "Sat Apr 06 10:39:32 EDT 2019" "EEE MMM dd HH:mm:ss zzz yyyy"
            out = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(message.messageTimeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime();
        holder.messageTime.setText(prettyTime.format(out));


        if (message.userId.equals(mListener.getUserId())) {
            holder.upVoteButton.setVisibility(View.INVISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.upVoteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.INVISIBLE);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.deleteClicked(messages.get(position));
            }
        });

        holder.upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.upvoteClicked(messages.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, messageText, messageTime, upvoteCount;
        ImageView deleteButton, upVoteButton, userProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameTextView);
            messageText = itemView.findViewById(R.id.messageTextView);
            deleteButton = itemView.findViewById(R.id.deleteBtnImageView);
            upVoteButton = itemView.findViewById(R.id.upvoteBtnImageView);
            messageTime = itemView.findViewById(R.id.messageTimeTextView);
            upvoteCount = itemView.findViewById(R.id.upVotesTextView);
            userProfileImage = itemView.findViewById(R.id.userImageView);
        }
    }

    public interface OnAdapterInteractionListener {
        void deleteClicked(Message message);

        void upvoteClicked(Message message);

        String getUserId();

        void getMessageProfileImage(ImageView imageView, String userId);

    }
}
