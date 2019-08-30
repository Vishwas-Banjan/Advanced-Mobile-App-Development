package com.example.inclass01;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    EditText resetEmail;
    Button resetPasswordLink;
    String emailReset;


    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        resetEmail = view.findViewById(R.id.editTextEmailReset);
        resetPasswordLink = view.findViewById(R.id.buttonPasswordReset);
        resetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resetEmail.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getContext(), "Enter Email", Toast.LENGTH_SHORT).show();
                    resetEmail.setError("This field can not be blank");
                } else {
                    emailReset = resetEmail.getText().toString();
                    User user = new User();
                    user.userEmail = emailReset;
                    mListener.resetEmail(user);
                }
            }
        });

        view.findViewById(R.id.cancelButtonResetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logInFragment();
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
        void resetEmail(User user);

        void logInFragment();


    }
}
