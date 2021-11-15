package com.example.familymapclient.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.familymapclient.R;

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

    public void loggedIn() {
        invalidateOptionsMenu();
        fm.beginTransaction().remove(fragment).commit();
        //fragment = new MapFragment();
        fm.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }
}