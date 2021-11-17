package com.example.familymapclient.UI;
import com.example.familymapclient.R;

import android.os.Bundle;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

import request.LoginRequest;
import request.RegisterRequest;

import Network.DataCache;


public class LoginFragment extends Fragment {

    public static final String ARG_TITLE = "title";

    private static final String LOG_TAG = "Login_Fragment";

    private String title;
    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText userNameText;
    private Button reg;
    private Button login;
    RadioGroup radioGender;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
        }

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, parent, false);
        serverHostEditText = view.findViewById(R.id.ServerIP);
        serverPortEditText = view.findViewById(R.id.ServerPort);
        firstNameEditText = view.findViewById(R.id.FirstName);
        lastNameEditText = view.findViewById(R.id.LastName);
        passwordEditText = view.findViewById(R.id.Password);
        emailEditText = view.findViewById(R.id.Email);
        userNameText = view.findViewById(R.id.Username);
        radioGender=(RadioGroup)view.findViewById(R.id.RadioGroup);
        final String[] gender = {"m"};
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int childCount = group.getChildCount();

                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        gender[0] = btn.getText().toString();// here gender will contain M or F.
                    }
                }
                Log.e("Gender", gender[0]);
            }
        });
        Button loginButton = view.findViewById(R.id.buttonLogin);
        loginButton.setEnabled(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String h = serverHostEditText.getText().toString().trim();
                String sp = serverPortEditText.getText().toString().trim();
                String p = passwordEditText.getText().toString().trim();
                String u = userNameText.getText().toString().trim();
                loginButton.setEnabled(!h.isEmpty() && !sp.isEmpty() && !p.isEmpty() && !u.isEmpty());

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        serverHostEditText.addTextChangedListener(watcher);
        serverPortEditText.addTextChangedListener(watcher);
        passwordEditText.addTextChangedListener(watcher);
        userNameText.addTextChangedListener(watcher);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache data = DataCache.getInstance();
                data.setServerHost(serverHostEditText.getText().toString());
                data.setServerPort(serverPortEditText.getText().toString());
                LoginRequest l = new LoginRequest(userNameText.getText().toString(),passwordEditText.getText().toString());
                //-l.setPassword(passwordEditText.getText().toString());
                //-l.setUsername(userNameText.getText().toString());
                LoginTask task = new LoginTask(getActivity());
                task.execute(l);
            }
        });
        Button registerButton = view.findViewById(R.id.buttonRegister);
        registerButton.setEnabled(false);
        TextWatcher watcher2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String h = serverHostEditText.getText().toString().trim();
                String sp = serverPortEditText.getText().toString().trim();
                String p = passwordEditText.getText().toString().trim();
                String u = userNameText.getText().toString().trim();
                String f = firstNameEditText.getText().toString().trim();
                String l = lastNameEditText.getText().toString().trim();
                String e = emailEditText.getText().toString().trim();
                registerButton.setEnabled(!h.isEmpty() && !sp.isEmpty() && !p.isEmpty() && !u.isEmpty()
                        && !f.isEmpty() && !l.isEmpty() && !e.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        serverHostEditText.addTextChangedListener(watcher2);
        serverPortEditText.addTextChangedListener(watcher2);
        passwordEditText.addTextChangedListener(watcher2);
        userNameText.addTextChangedListener(watcher2);
        firstNameEditText.addTextChangedListener(watcher2);
        lastNameEditText.addTextChangedListener(watcher2);
        emailEditText.addTextChangedListener(watcher2);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ////TODO THIS IS BROKEN AND IS ASSIGNING THE WRONG GENDER
                String finalGender = gender[0];
                if (finalGender.equals("m")){
                    finalGender = "m";
                }
                else {
                    finalGender = "f";
                }
                DataCache data = DataCache.getInstance();
                data.setServerHost(serverHostEditText.getText().toString());
                data.setServerPort(serverPortEditText.getText().toString());
                data.setUserFirst(firstNameEditText.getText().toString());
                data.setUserLast(lastNameEditText.getText().toString());

                RegisterRequest l = new RegisterRequest(userNameText.getText().toString(),passwordEditText.getText().toString(),
                        emailEditText.getText().toString(), firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),finalGender, firstNameEditText.getText().toString() + lastNameEditText.getText().toString());

                RegisterTask task = new RegisterTask(getActivity());
                task.execute(l);
            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "in onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "in onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "in onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "in onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(LOG_TAG, "in onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "in onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(LOG_TAG, "in onDetach()");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(LOG_TAG, "in onSaveInstanceState(Bundle)");
    }
}