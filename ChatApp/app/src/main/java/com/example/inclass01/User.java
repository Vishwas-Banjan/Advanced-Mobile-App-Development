package com.example.inclass01;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class User {
    String firstName, lastName, gender, city, userId, userEmail, userPassword, profileImage;
    ArrayList<String> messages;


    public User() {
    }

    public User(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", city='" + city + '\'' +
                ", userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", messages=" + messages +
                '}';
    }
}
