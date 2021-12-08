package com.example.familymapclient.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymapclient.R;


public class MainActivity extends AppCompatActivity {
    private static MainActivity instance = null;

    public static MainActivity getInstance() {
        return instance;
    }

    public void switchToMap(String userName){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        MapFragment mapFragment = createMapFragment(userName);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.fragmentContainer, mapFragment , null);
        // Commit the transaction
        transaction.commit();
    }

    public void switchToLogin(String userName){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        LoginFragment loginFragment = createLoginFragment(userName);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.fragmentContainer, loginFragment , null);
        // Commit the transaction
        transaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null) {
            LoginFragment logFragment = new LoginFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, logFragment)
                    .commit();
        }

        if (intent.getStringExtra("REFRESH") != null){
            switchToMap("emp");
        }
        else {
            switchToLogin("emp");
        }
    }






    public static LoginFragment createLoginFragment(String title) {
        LoginFragment fragment = new LoginFragment();

        Bundle args = new Bundle();
        args.putString(LoginFragment.ARG_TITLE, title);
        fragment.setArguments(args);

        return fragment;
    }
    public static MapFragment createMapFragment(String title) {
        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();
        args.putString(MapFragment.ARG_TITLE, title);
        fragment.setArguments(args);

        return fragment;
    }



}