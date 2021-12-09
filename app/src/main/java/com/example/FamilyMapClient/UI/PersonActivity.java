package com.example.FamilyMapClient.UI;
import com.example.FamilyMapClient.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import Network.DataCache;
import modelClass.Event;
import modelClass.Person;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;




public class PersonActivity extends AppCompatActivity {
    DataCache data = DataCache.getInstance();
    Person person = null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Iconify.with(new FontAwesomeModule());

        String personID = getIntent().getStringExtra("THIS_PERSON");
        ArrayList<Person> people = data.getPeople();

        for (Person p: people){
            if (p.getPersonID().equals(personID)){
                person = p;
            }
        }
        if (person != null){
            TextView textview = findViewById(R.id.Name);
            textview.setText(person.getFirstName());
            textview = findViewById(R.id.LastName);
            textview.setText(person.getLastName());
            textview = findViewById(R.id.Gender);
            textview.setText(person.getGender().toUpperCase(Locale.ROOT));
            ExpandableListView expandableListView = findViewById(R.id.expandableListView);
            ArrayList<Event> Events = data.getEvents();
            ArrayList<Event> events = new ArrayList<>();
            ArrayList<Person> persons = new ArrayList<>();

            for (Event e : Events){
                if (e.getPersonID().equals(person.getPersonID())){
                    events.add(e);
                }
            }
            Comparator<Event> EventSorter = Comparator.comparingInt(Event::getYear);
            events.sort(EventSorter);
            for (Person p: people) {
                if (p.getPersonID().equals(person.getFatherID())) {
                    persons.add(p);
                }
                else if (p.getPersonID().equals(person.getMotherID())) {
                    persons.add(p);
                }
                else if (p.getPersonID().equals(person.getSpouseID())) {
                    persons.add(p);
                }
                if (p.getFatherID() != null) {
                    if (p.getFatherID().equals(person.getPersonID())) {
                        persons.add(p);
                    }
                }
                if (p.getMotherID() != null) {
                    if (p.getMotherID().equals(person.getPersonID())) {
                        persons.add(p);
                    }
                }
            }
            expandableListView.setAdapter(new ExpandableListAdapter(events,persons));
        }
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
    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int FAMILY_GROUP_POSITION = 0;
        private static final int EVENT_GROUP_POSITION = 1;

        private final List<Event> events;
        private final List<Person> people;

        ExpandableListAdapter(ArrayList<Event> events, ArrayList<Person> people) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case FAMILY_GROUP_POSITION:
                    return people.size();
                case EVENT_GROUP_POSITION:
                    return events.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case FAMILY_GROUP_POSITION:
                    return getString(R.string.FamilyTitle);
                case EVENT_GROUP_POSITION:
                    return getString(R.string.EventTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case FAMILY_GROUP_POSITION:
                    return people.get(childPosition);
                case EVENT_GROUP_POSITION:
                    return events.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_dropdown, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.FamilyTitle);
                    break;
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.EventTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_family, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_events, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeFamilyView(View FamilyItemView, final int childPosition) {
            TextView FamilyView = FamilyItemView.findViewById(R.id.FamilyTitle);

            FamilyView.setText(new StringBuilder().append(people.get(childPosition).getFirstName())
                    .append(" ")
                    .append(people.get(childPosition).getLastName()).toString());

            TextView FamilyRelationshipView = FamilyItemView.findViewById(R.id.FamilyRelationship);
            String RelationshipType = "";

            if (people.get(childPosition).getPersonID().equals(person.getFatherID())){
                RelationshipType = "Father";
            }
            else if (people.get(childPosition).getPersonID().equals(person.getMotherID())){
                RelationshipType = "Mother";
            }
            if (people.get(childPosition).getFatherID() != null && people.get(childPosition).getMotherID() != null) {
                if (people.get(childPosition).getFatherID().equals(person.getPersonID()) || people.get(childPosition).getMotherID().equals(person.getPersonID())) {
                    RelationshipType = "Child";
                }
            }
            if (people.get(childPosition).getPersonID().equals(person.getSpouseID())){
                RelationshipType = "Spouse";
            }
            FamilyRelationshipView.setText(RelationshipType);

            ImageView familyImageView = FamilyItemView.findViewById(R.id.iconField);
            Drawable genderIcon;

            if (people.get(childPosition).getGender().toUpperCase(Locale.ROOT).equals("M")){
                genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male)
                        .colorRes(R.color.black)
                        .sizeDp(40);
            }else{
                genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female)
                        .colorRes(R.color.black)
                        .sizeDp(40);
            }
            familyImageView.setImageDrawable(genderIcon);


            FamilyItemView.setOnClickListener(v -> {
                Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                intent.putExtra("THIS_PERSON", people.get(childPosition).getPersonID());
                startActivity(intent);
            });

        }

        private void initializeEventView(View EventItemView, final int childPosition) {

            ImageView eventImageView = EventItemView.findViewById(R.id.iconField);
            Drawable eventIcon;


            eventIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker)
                            .colorRes(R.color.black)
                            .sizeDp(40);
            eventImageView.setImageDrawable(eventIcon);



            TextView EventIDView = EventItemView.findViewById(R.id.EventsTitle);
            EventIDView.setText(new StringBuilder().append(events.get(childPosition).getEventType().toUpperCase(Locale.ROOT))
                    .append(": ")
                    .append(events.get(childPosition).getCity())
                    .append(", ")
                    .append(events.get(childPosition).getCountry())
                    .append(" (")
                    .append(events.get(childPosition).getYear())
                    .append(")").toString());


            TextView EventTypeView = EventItemView.findViewById(R.id.EventsType);

            EventTypeView.setText(new StringBuilder().append(person.getFirstName())
                    .append(" ")
                    .append(person.getLastName()).toString());
            EventItemView.setOnClickListener(v -> {
                Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                intent.putExtra("THIS_EVENT", events.get(childPosition).getEventID());
                finish();
                startActivity(intent);});
        }



        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}