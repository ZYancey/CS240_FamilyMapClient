package com.example.familymapclient.UI;
import com.example.familymapclient.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import Network.DataCache;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataCache data = DataCache.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch LifeStoryLines = (Switch) findViewById(R.id.switchLifeStoryLines);
        LifeStoryLines.setChecked(data.getLifeStoryLines());
        LifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setLifeStoryLines(isChecked);
            }
        });
        Switch FamilyTreeLines = (Switch) findViewById(R.id.switchFamilyTreeLines);
        FamilyTreeLines.setChecked(data.getFamilyTreeLines());
        FamilyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setFamilyTreeLines(isChecked);
            }
        });
        Switch SpouseLines = (Switch) findViewById(R.id.switchSpouseLines);
        SpouseLines.setChecked(data.getSpouseLines());
        SpouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setSpouseLines(isChecked);
            }
        });
        Switch FathersSide = (Switch) findViewById(R.id.switchFathersSide);
        FathersSide.setChecked(data.getFathersSide());
        FathersSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setFathersSide(isChecked);
            }
        });
        Switch MothersSide = (Switch) findViewById(R.id.switchMothersSide);
        MothersSide.setChecked(data.getMothersSide());
        MothersSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setMothersSide(isChecked);
            }
        });
        Switch MaleEvents = (Switch) findViewById(R.id.switchMaleEvents);
        MaleEvents.setChecked(data.getMaleEvents());
        MaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setMaleEvents(isChecked);
            }
        });
        Switch FemaleEvents = (Switch) findViewById(R.id.switchFemaleEvents);
        FemaleEvents.setChecked(data.getFemaleEvents());
        FemaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setFemaleEvents(isChecked);
            }
        });
        TextView Logout = (TextView) findViewById(R.id.Logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        });
        TextView Logout1 = (TextView) findViewById(R.id.Logout1);
        Logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            ///TODO FIX RESUME STATE FOR REDRAW
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("REFRESH", "REFRESH");
            startActivity(intent);
        }

        return true;
    }

}