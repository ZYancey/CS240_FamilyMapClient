package com.example.familymapclient.UI;
import com.example.familymapclient.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

import Network.DataCache;
import modelClass.Event;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataCache data = DataCache.getInstance();
        super.onCreate(savedInstanceState);
        String eventID = getIntent().getStringExtra("THIS_EVENT");
        setContentView(R.layout.activity_event);
        FragmentManager fm = this.getSupportFragmentManager();
        MapFragment frag = (MapFragment) fm.findFragmentById(R.id.fragment_map);
        if (frag == null) {
            frag = EventActivity.createMapFragment(data.getUsername(), eventID);
            fm.beginTransaction()
                    .add(R.id.fragment_map,frag)
                    .commit();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            /*
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("REFRESH", "REFRESH");
            startActivity(intent);
        }

        return true;
    }
    public static MapFragment createMapFragment(String title, String eventID) {
        MapFragment fragment = new MapFragment();

        Bundle args = new Bundle();
        args.putString(MapFragment.ARG_TITLE, title);

        args.putString("EVENT", eventID);
        fragment.setArguments(args);

        return fragment;
    }
}