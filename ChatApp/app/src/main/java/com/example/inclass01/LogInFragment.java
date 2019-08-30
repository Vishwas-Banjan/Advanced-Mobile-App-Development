package com.example.inclass01;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {
    EditText userEmail, userPassword;
    Button logInButton, signUpLogin;
    String logInEmail, logInPassword;
    TextView forgotPassword;
    private OnFragmentInteractionListener mListener;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        userEmail = view.findViewById(R.id.editTextEmailLogIn);
        userPassword = view.findViewById(R.id.editTextPasswordLogIn);
        logInButton = view.findViewById(R.id.buttonLogin);
        signUpLogin = view.findViewById(R.id.buttonSignUpLogIn);
        forgotPassword = view.findViewById(R.id.textViewForgotPassword);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    userEmail.setError("This field can not be blank");
                } else if (userPassword.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                    userPassword.setError("This field can not be blank");
                } else {
                    logInEmail = userEmail.getText().toString();
                    logInPassword = userPassword.getText().toString();
                    User user = new User();
                    user.userEmail = logInEmail;
                    user.userPassword = logInPassword;
                    mListener.logIn(user);
                }
            }
        });

        signUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SignUpFragment();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.ResetFragment();
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
        void logIn(User user);

        void SignUpFragment();

        void ResetFragment();

    }
}
