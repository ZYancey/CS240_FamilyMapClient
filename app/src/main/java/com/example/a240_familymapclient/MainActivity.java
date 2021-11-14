package com.example.a240_familymapclient;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

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
        //if (fragment == null) {
            //if (DataCache.hasUser()) {
            //    fragment = new MapFragment();
            //}
            //else {
                fragment = new LoginFragment();
            //}
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        //}
    }
}