package com.example.FamilyMapClient.UI;
import com.example.FamilyMapClient.R;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import Network.DataCache;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataCache data = DataCache.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch LifeStoryLines = findViewById(R.id.switchLifeStoryLines);
        LifeStoryLines.setChecked(data.getEventLines());
        LifeStoryLines.setOnCheckedChangeListener((buttonView, isChecked) -> data.setEventLines(isChecked));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch FamilyTreeLines = findViewById(R.id.switchFamilyTreeLines);
        FamilyTreeLines.setChecked(data.getFamilyTreeLines());
        FamilyTreeLines.setOnCheckedChangeListener((buttonView, isChecked) -> data.setFamilyTreeLines(isChecked));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch SpouseLines = findViewById(R.id.switchSpouseLines);
        SpouseLines.setChecked(data.getSpouseLines());
        SpouseLines.setOnCheckedChangeListener((buttonView, isChecked) -> data.setSpouseLines(isChecked));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch FathersSide = findViewById(R.id.switchFathersSide);
        FathersSide.setChecked(data.getFathersSide());
        FathersSide.setOnCheckedChangeListener((buttonView, isChecked) -> data.setFathersSide(isChecked));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch MothersSide = findViewById(R.id.switchMothersSide);
        MothersSide.setChecked(data.getMothersSide());
        MothersSide.setOnCheckedChangeListener((buttonView, isChecked) -> data.setMothersSide(isChecked));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch MaleEvents = findViewById(R.id.switchMaleEvents);
        MaleEvents.setChecked(data.getMaleEvents());
        MaleEvents.setOnCheckedChangeListener((buttonView, isChecked) -> data.setMaleEvents(isChecked));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch FemaleEvents = findViewById(R.id.switchFemaleEvents);
        FemaleEvents.setChecked(data.getFemaleEvents());
        FemaleEvents.setOnCheckedChangeListener((buttonView, isChecked) -> data.setFemaleEvents(isChecked));

        TextView Logout = findViewById(R.id.Logout);
        Logout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            finishAffinity();
            startActivity(intent);
        });
        TextView Logout1 = findViewById(R.id.Logout1);
        Logout1.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            finishAffinity();
            startActivity(intent);
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("REFRESH", "REFRESH");
            startActivity(intent);
        }
        return true;
    }

}