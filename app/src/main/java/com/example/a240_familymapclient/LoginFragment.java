package com.example.a240_familymapclient;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

//import com.familymap.family_map.R;
//import com.familymap.family_map.model.DataCache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import RequestResult.LoginRequest;
//import RequestResult.RegisterRequest;

public class LoginFragment extends Fragment {
    EditText hostName;
    EditText port;
    EditText username;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText email;
    String serverHostEditText;
    String serverPortEditText;
    String usernameEditText;
    String passwordEditText;
    String firstNameEditText;
    String lastNameEditText;
    String emailEditText;
    String gender;

    private static final String USER_RESULT = "UserResult";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.login_fragment, parent, false);

        RadioGroup rg = v.findViewById(R.id.RadioGroup);
        rg.setOnCheckedChangeListener((rg1, checkedId) -> {
            RadioButton rb = v.findViewById(checkedId);
            // Check which radio button was clicked
            if (checkedId == R.id.radioMale) {
                gender = "m";
            }
            else if (checkedId == R.id.radioFemale) {
                gender = "f";
            }
            v.findViewById(R.id.buttonRegister).setEnabled(serverHostEditText != null && serverPortEditText != null && usernameEditText
                    != null && passwordEditText.length() > 0 && firstNameEditText != null && lastNameEditText
                    != null && emailEditText != null && gender != null);
        });

        TextWatcher text = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @SuppressLint("NonConstantResourceId")
            @Override
            public void afterTextChanged(Editable s) {

                hostName = v.findViewById(R.id.ServerIP);
                serverHostEditText = hostName.getText().toString();

                port = v.findViewById(R.id.ServerPort);
                serverPortEditText = port.getText().toString();

                username = v.findViewById(R.id.Username);
                usernameEditText = username.getText().toString();

                password = v.findViewById(R.id.Password);
                passwordEditText = password.getText().toString();

                v.findViewById(R.id.buttonLogin).setEnabled(serverHostEditText.length() > 0 && serverPortEditText.length() > 0 && usernameEditText.length() > 0
                        && passwordEditText.length() > 0);

                firstName = v.findViewById(R.id.FirstName);
                firstNameEditText = firstName.getText().toString();

                lastName = v.findViewById(R.id.LastName);
                lastNameEditText = lastName.getText().toString();

                email = v.findViewById(R.id.Email);
                emailEditText = email.getText().toString();

                v.findViewById(R.id.buttonRegister).setEnabled(serverHostEditText.length() > 0 && serverPortEditText.length() > 0 && usernameEditText.length() > 0
                        && passwordEditText.length() > 0 && firstNameEditText.length() > 0 && lastNameEditText.length() > 0
                        && emailEditText.length() > 0 && gender != null);
            }
        };

        hostName = v.findViewById(R.id.ServerIP);
        port = v.findViewById(R.id.ServerPort);
        username = v.findViewById(R.id.Username);
        password = v.findViewById(R.id.Password);
        firstName = v.findViewById(R.id.FirstName);
        lastName = v.findViewById(R.id.LastName);
        email = v.findViewById(R.id.Email);
        hostName.addTextChangedListener(text);
        port.addTextChangedListener(text);
        username.addTextChangedListener(text);
        password.addTextChangedListener(text);
        firstName.addTextChangedListener(text);
        lastName.addTextChangedListener(text);
        email.addTextChangedListener(text);

        Button loginButton = v.findViewById(R.id.buttonLogin);

        /*
        loginButton.setOnClickListener(v1 -> {

            // Set up a handler that will process messages from the task and make updates on the UI thread
            @SuppressLint("HandlerLeak")
            Handler uiThreadMessageHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    Bundle bundle = msg.getData();
                    String finalMessage = bundle.getString(USER_RESULT);
                    if (DataCache.hasUser()) {
                        ((MainActivity)getActivity()).loggedIn();
                    }
                    Toast.makeText(getActivity(), finalMessage, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });



            LoginRequest lR = new LoginRequest(usernameEditText, passwordEditText);

            // Create and execute the task on a separate thread
            LoginAsyncTask task = new LoginAsyncTask(uiThreadMessageHandler, lR,
                    serverHostEditText, serverPortEditText);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task);
        });
        */


        Button registerButton = v.findViewById(R.id.buttonRegister);

        /*registerButton.setOnClickListener(v12 -> {

            // Set up a handler that will process messages from the task and make updates on the UI thread
            @SuppressLint("HandlerLeak")
            Handler uiThreadMessageHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    Bundle bundle = msg.getData();
                    String finalMessage = bundle.getString(USER_RESULT);
                    if (DataCache.hasUser()) {
                        ((MainActivity)getActivity()).loggedIn();
                    }
                    Toast.makeText(getActivity(), finalMessage, Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            RegisterRequest rR = new RegisterRequest(usernameEditText, passwordEditText,
                    emailEditText, firstNameEditText, lastNameEditText, gender, null);

            // Create and execute the task on a separate thread
            RegisterAsyncTask task = new RegisterAsyncTask(uiThreadMessageHandler, rR,
                    serverHostEditText, serverPortEditText);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(task);
        });*/

        return v;
    }
}

/*
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment,container,false);
        return view;
    }
}*/