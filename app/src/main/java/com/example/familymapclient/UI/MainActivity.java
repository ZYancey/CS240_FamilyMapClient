package com.example.familymapclient.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.familymapclient.R;

import Network.DataCache;

public class MainActivity extends AppCompatActivity {
/*
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

    public MainActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invalidateOptionsMenu();
        setContentView(R.layout.activity_main);
        //Iconify.with(new FontAwesomeModule());
        if (fragment == null) {
            if (DataCache.hasUser()) {
                fragment = new MapFragment();
            }
            else {
                fragment = new LoginFragment();
            }
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    public void loggedIn(){
        invalidateOptionsMenu();
        fm.beginTransaction().remove(fragment).commit();
        fragment = new MapFragment();
        fm.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }
    */

    private static MainActivity instance = null;
    private final int REQ_CODE_ORDER_INFO = 1;

    public static MainActivity getInstance() {
        return instance;
    }

    public static void setInstance(MainActivity instance) {
        MainActivity.instance = instance;
    }

    public void switchToMap(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        MapFragment mapFragment = new MapFragment();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);

        // Replace whatever is in the fragment_container view with this fragment
        transaction.replace(R.id.fragmentContainer, mapFragment , null);

        // Commit the transaction
        transaction.commit();
    }

    public void switchToLogin(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        LoginFragment loginFragment = new LoginFragment();

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
            switchToMap();
        }
        else {
            switchToLogin();
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

        //Bundle args = new Bundle();
        //args.putString(MapFragment.ARG_TITLE, title);
        //fragment.setArguments(args);

        return fragment;
    }



}