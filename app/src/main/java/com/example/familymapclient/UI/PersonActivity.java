package com.example.familymapclient.UI;
import com.example.familymapclient.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import Network.DataCache;
import modelClass.Event;
import modelClass.Person;

public class PersonActivity extends AppCompatActivity {
    DataCache data = DataCache.getInstance();
    Person person = null;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        String personID = getIntent().getStringExtra("THIS_PERSON");
        ArrayList<Person> people = data.getPeople();

        for (Person p: people){
            if (p.getPersonID().equals(personID)){
                person = p;
            }
        }
        if (person != null){
            TextView textview = (TextView) findViewById(R.id.Name);
            textview.setText(person.getFirstName());
            textview = (TextView) findViewById(R.id.LastName);
            textview.setText(person.getLastName());
            textview = (TextView) findViewById(R.id.Gender);
            textview.setText(person.getGender());
            ExpandableListView expandableListView = findViewById(R.id.expandableListView);
            ArrayList<Event> Events = data.getEvents();
            ArrayList<Event> events = new ArrayList<>();
            ArrayList<Person> persons = new ArrayList<>();

            for (Event e : Events){
                if (e.getPersonID().equals(person.getPersonID())){
                    events.add(e);
                }
            }
            Comparator<Event> EventSorter = new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return Integer.compare(o1.getYear(),o2.getYear());
                }
            };
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
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
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
                    itemView = getLayoutInflater().inflate(R.layout.family_item, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.events_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeFamilyView(View FamilyItemView, final int childPosition) {
            TextView FamilyView = FamilyItemView.findViewById(R.id.FamilyTitle);
            FamilyView.setText(people.get(childPosition).getFirstName() + " " + people.get(childPosition).getLastName());

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

            FamilyItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("THIS_PERSON", people.get(childPosition).getPersonID());
                    startActivity(intent);
                }
            });

        }

        private void initializeEventView(View EventItemView, final int childPosition) {
            TextView EventIDView = EventItemView.findViewById(R.id.EventsTitle);
            EventIDView.setText(events.get(childPosition).getEventType() + ": " +
                    events.get(childPosition).getCity() + ", " + events.get(childPosition).getCountry()
                    + " (" + events.get(childPosition).getYear() + ")");
            TextView EventTypeView = EventItemView.findViewById(R.id.EventsType);
            EventTypeView.setText(person.getFirstName() + " " + person.getLastName());
            EventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("THIS_EVENT", events.get(childPosition).getEventID());
                    finish();
                    startActivity(intent);                }
            });


        }



        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}