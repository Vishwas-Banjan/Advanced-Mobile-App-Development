package com.example.inclass01;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.media.MediaRecorder.VideoSource.CAMERA;


public class SignUpFragment extends Fragment {
    EditText userFirstName, userLastName, userEmail, userPassword, userConfirmPassword, city;
    RadioGroup genderRadioGroup;
    Button signUpButton, cancelButton;
    ImageButton profileImage;
    String FirstName, LastName, Email, Password, Gender, City;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://amad-inclass01.appspot.com");
    private FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        userFirstName = view.findViewById(R.id.editTextFirstName);
        userLastName = view.findViewById(R.id.editTextLastName);
        userEmail = view.findViewById(R.id.editTextEmailLogIn);
        userPassword = view.findViewById(R.id.editTextPassword);
        userConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        signUpButton = view.findViewById(R.id.buttonSignUp);
        cancelButton = view.findViewById(R.id.buttonCancel);
        genderRadioGroup = view.findViewById(R.id.radioGroup);
        city = view.findViewById(R.id.editTextCity);
        profileImage = view.findViewById(R.id.profileImage);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logInFragment();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.selectImage(profileImage);

            }
        });

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.maleRadioButton:
                        Gender = "Male";
                        break;
                    case R.id.femaleRadioButton:
                        Gender = "Female";
                        break;
                    default:
                        break;
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userFirstName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter First Name", Toast.LENGTH_SHORT).show();
                    userFirstName.setError("This field can not be blank");
                } else if (userLastName.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Last Name", Toast.LENGTH_SHORT).show();
                    userLastName.setError("This field can not be blank");
                } else if (userEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email ID", Toast.LENGTH_SHORT).show();
                    userEmail.setError("This field can not be blank");
                } else if ((userPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                    Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    userPassword.setError("This field can not be blank");
                } else if ((userConfirmPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                    Toast.makeText(getContext(), "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    userConfirmPassword.setError("This field can not be blank");
                } else if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getContext(), "Select Gender", Toast.LENGTH_SHORT).show();
                } else {
                    if (userPassword.getText().toString().equals(userConfirmPassword.getText().toString())) {
                        FirstName = userFirstName.getText().toString().trim();
                        LastName = userLastName.getText().toString().trim();
                        Email = userEmail.getText().toString().trim();
                        Password = userPassword.getText().toString().trim();
                        City = city.getText().toString().trim();
                        User user = new User();
                        user.firstName = FirstName;
                        user.lastName = LastName;
                        user.userEmail = Email;
                        user.userPassword = Password;
                        user.gender = Gender;
                        user.city = City;
                        mListener.signUp(user);
                    } else {
                        userPassword.setError("Passwords do not match");
                        userConfirmPassword.setError("Passwords do not match");
                    }
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

    public interface OnFragmentInteractionListener {
        void signUp(User user);

        void selectImage(ImageView imageUpload);

        void logInFragment();
    }

}
