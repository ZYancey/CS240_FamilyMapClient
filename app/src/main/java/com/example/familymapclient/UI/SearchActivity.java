package com.example.familymapclient.UI;
import com.example.familymapclient.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Network.DataCache;
import modelClass.Event;
import modelClass.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int EVENT_ITEM_VIEW_TYPE = 0;
    private static final int PERSON_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        DataCache data = DataCache.getInstance();
        ArrayList<Event> Events = data.getEvents();
        ArrayList<Person> People = data.getPeople();
        ArrayList<Event> events = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();
        SearchView searchView = findViewById(R.id.search_bar);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);



        SearchAdapter adapter = new SearchAdapter(people, events);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//              if (searchView.isExpanded() && TextUtils.isEmpty(newText)) {
                callSearch(newText);
//              }
                return true;
            }

            public void callSearch(String query) {
                query = query.toLowerCase();
                events.clear();
                people.clear();
                for (Event e : Events){
                    if (e.getCountry().toLowerCase().contains(query)){
                        events.add(e);
                    }
                    else if (e.getCity().toLowerCase().contains(query)){
                        events.add(e);
                    }
                    else if (e.getEventType().toLowerCase().contains(query)){
                        events.add(e);
                    }
                    else if (Integer.toString(e.getYear()).contains(query)){
                        events.add(e);
                    }
                }
                for (Person p : People){
                    if (p.getFirstName().toLowerCase().contains(query)){
                        people.add(p);
                    }
                    else if (p.getLastName().toLowerCase().contains(query)){
                        people.add(p);
                    }
                }
                adapter.notifyDataSetChanged();
            }

        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)  {
            /*
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

             */
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("REFRESH", "REFRESH");
            startActivity(intent);
        }

        return true;
    }
    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<Person> people;
        private final List<Event> events;

        SearchAdapter(List<Person> people, List<Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView location;

        private final int viewType;
        private Person Person;
        private Event Event;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                name = itemView.findViewById(R.id.PersonTitle);
                location = null;
            } else {
                name = itemView.findViewById(R.id.EventTitle);
                location = itemView.findViewById(R.id.EventPerson);

            }
        }

        private void bind(Person Person) {
            this.Person = Person;
            name.setText(Person.getFirstName() + " " + Person.getLastName());
        }

        private void bind(Event Event) {
            this.Event = Event;
            name.setText(Event.getEventType() + ": " + Event.getCity() + ", " + Event.getCountry() + " (" + Event.getYear() + ")");
            DataCache data = DataCache.getInstance();
            ArrayList<Person> People = data.getPeople();
            Person person = null;
            for (Person p : People){
                if (p.getPersonID().equals(Event.getPersonID())){
                    person = p;
                }
            }
            location.setText(person.getFirstName() + " " + person.getLastName());
        }

        @Override
        public void onClick(View view) {
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("THIS_PERSON", Person.getPersonID());
                startActivity(intent);
            } else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("THIS_EVENT", Event.getEventID());
                finish();
                startActivity(intent);


            }
        }
    }
}