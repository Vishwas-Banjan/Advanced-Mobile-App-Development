package com.example.inclass01;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class DisplayMyAccount extends Fragment {

    private OnFragmentInteractionListener mListener;
    User user;

    public DisplayMyAccount(User user) {
        this.user = user;
    }

    ImageView profileImage;
    TextView fistName, lastName, email, gender, city;
    EditText editFistName, editLastName, editCity;
    RadioGroup editGender;
    Button editButton, saveButton, cancelButton;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_my_account, container, false);
        profileImage = view.findViewById(R.id.userImageImageView);
        fistName = view.findViewById(R.id.userFirstNameTextView);
        lastName = view.findViewById(R.id.userLastNametextView);
        email = view.findViewById(R.id.userEmailTextView);
        gender = view.findViewById(R.id.userGenderTextView);
        city = view.findViewById(R.id.userCityTextView);
        editButton = view.findViewById(R.id.editProfileButton);

        editFistName = view.findViewById(R.id.userFirstNameEditText);
        editLastName = view.findViewById(R.id.userLastNameEditText);
        editGender = view.findViewById(R.id.genderRadioGroup);
        editCity = view.findViewById(R.id.userCityEditText);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        editButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        editFistName.setVisibility(View.INVISIBLE);
        editLastName.setVisibility(View.INVISIBLE);
        editCity.setVisibility(View.INVISIBLE);
        editGender.setVisibility(View.INVISIBLE);
        profileImage.setEnabled(false);

        fistName.setText(user.firstName);
        lastName.setText(user.lastName);
        email.setText(user.userEmail);
        gender.setText(user.gender);
        city.setText(user.city);


        if (user.profileImage!=null) {
            Picasso.get().load(user.profileImage).into(profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    Log.d("demo", "onSuccess: Success!");
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }else profileImage.setImageDrawable(getActivity().getDrawable(R.drawable.download1));

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                editFistName.setVisibility(View.VISIBLE);
                editLastName.setVisibility(View.VISIBLE);
                editCity.setVisibility(View.VISIBLE);
                editGender.setVisibility(View.VISIBLE);
                profileImage.setEnabled(true);


                fistName.setVisibility(View.INVISIBLE);
                lastName.setVisibility(View.INVISIBLE);
                gender.setVisibility(View.INVISIBLE);
                city.setVisibility(View.INVISIBLE);

                editFistName.setText(user.firstName);
                editLastName.setText(user.lastName);
                editCity.setText(user.city);

                if (user.gender.equals("Male")) {
                    editGender.check(R.id.maleRadioButton);
                } else {
                    editGender.check(R.id.femaleRadioButton);
                }
            }
        });


            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.selectImage(profileImage);
                }
            });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                editFistName.setVisibility(View.INVISIBLE);
                editLastName.setVisibility(View.INVISIBLE);
                editCity.setVisibility(View.INVISIBLE);
                editGender.setVisibility(View.INVISIBLE);
                profileImage.setEnabled(false);

                fistName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                gender.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);


                User editedUser = new User();
                editedUser.firstName = editFistName.getText().toString().trim();
                editedUser.lastName = editLastName.getText().toString().trim();
                editedUser.city = editCity.getText().toString().trim();
                editedUser.userEmail = user.userEmail;
                editedUser.profileImage=user.profileImage;

                if (editGender.getCheckedRadioButtonId() == R.id.maleRadioButton) {
                    editedUser.gender = "Male";
                } else {
                    editedUser.gender = "Female";
                }

                fistName.setText(editedUser.firstName);
                lastName.setText(editedUser.lastName);
                email.setText(editedUser.userEmail);
                gender.setText(editedUser.gender);
                city.setText(editedUser.city);

                mListener.updateUser(editedUser);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editButton.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);
                profileImage.setEnabled(false);

                editFistName.setVisibility(View.INVISIBLE);
                editLastName.setVisibility(View.INVISIBLE);
                editCity.setVisibility(View.INVISIBLE);
                editGender.setVisibility(View.INVISIBLE);

                fistName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                gender.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
            }
        });

        if (mListener.getUserId().equals(user.userId)) {
            editButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setVisibility(View.INVISIBLE);
        }

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
        String getUserId();

        void selectImage(ImageView imageUpload);

        void updateUser(User user);
    }
}
