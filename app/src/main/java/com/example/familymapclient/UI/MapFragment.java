package com.example.familymapclient.UI;
import com.example.familymapclient.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;

import Network.DataCache;
import modelClass.Event;
import modelClass.Person;
import java.util.Comparator;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private final List<Polyline> polylines = new ArrayList<>();
    public static final String ARG_TITLE = "title";
    private GoogleMap map;
    String eventID = null;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        assert getArguments() != null;
        eventID = getArguments().getString("EVENT");
        if (eventID == null) {
            setHasOptionsMenu(true);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        TextView textview = view.findViewById(R.id.mapTextView);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.main_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.searchItem:
                map.clear();
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
                return true;
            case R.id.settingsItem:
                map.clear();
                intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        DataCache data = DataCache.getInstance();
        ArrayList<String> eventstypes = new ArrayList<>();
        ArrayList<Float> colors = new ArrayList<>();
        ArrayList<Event> events;
        events = data.getUserEvents();
        if (data.getFathersSide()){
            events.addAll(data.setFatherEvents());
            data.setEvents(events);
        }
        if (data.getMothersSide()){
            events.addAll(data.setMotherEvents());
            data.setEvents(events);
        }
        if (!data.getMaleEvents() && !data.getFemaleEvents()){
            events.clear();
            data.setEvents(events);
        }
        else if (data.getMaleEvents() && !data.getFemaleEvents()){
            events = data.setMaleEvents(events);
            data.setEvents(events);

        }
        else if (!data.getMaleEvents() && data.getFemaleEvents()){
            events = data.setFemaleEvents(events);
            data.setEvents(events);
        }
        data.setEvents(events);


        // Add a marker in Sydney and move the camera
        for (Event e : data.getEvents()) {
            LatLng start_point = new LatLng(e.getLatitude(), e.getLongitude());
            Marker m;
            int check = -1;
            for (int i = 0; i < eventstypes.size(); ++i) {
                if (e.getEventType().toUpperCase(Locale.ROOT).equals(eventstypes.get(i))){
                    check = i;
                }
            }
            if (check == -1) {
                eventstypes.add(e.getEventType().toUpperCase(Locale.ROOT));
                float color = (float)colors.size() * 60;
                while (color >= 360){
                    color = color / 2;
                }
                m = map.addMarker(new MarkerOptions().position(start_point).title(e.getEventID()).icon(BitmapDescriptorFactory
                        .defaultMarker(color)));
                colors.add(color);
            }
            else {
                m = map.addMarker(new MarkerOptions().position(start_point).title(e.getEventID()).icon(BitmapDescriptorFactory
                        .defaultMarker(colors.get(check))));
            }
            assert m != null;
            m.setTag(e);
            //Life Story Line

            //Family Tree Line
            if (events.contains(data.getUserEvents().get(0))) {
                start_point = new LatLng(data.getUserEvents().get(0).getLatitude(), data.getUserEvents().get(0).getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(start_point));
            }
            else {
                start_point = new LatLng(events.get(0).getLatitude(), events.get(0).getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(start_point));
            }

            e = null;
            for (Event event : data.getEvents()){
                if (event.getEventID().equals(eventID)){
                    e = event;
                }
            }
            if (e != null){
                LatLng event = new LatLng(e.getLatitude(), e.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(event));
                TextView textview = view.findViewById(R.id.mapTextView);
                Person g = null;
                for (Person p :data.getPeople()) {
                    if (p.getPersonID().equals(e.getPersonID())){
                        g = p;
                    }
                }
                for(Polyline line : polylines) {
                    line.remove();
                }
                polylines.clear();

                if (data.getSpouseLines()) {
                    //Spouse Line
                    if (g != null && (g.getSpouseID() != null)){
                        LatLng sydney = new LatLng(e.getLatitude(), e.getLongitude());
                        Person s = null;
                        for (Person p : data.getPeople()) {
                            if (g.getSpouseID().equals(p.getPersonID())) {
                                s = p;
                            }
                        }
                        Event j = null;
                        ArrayList<Event> lineEvents = new ArrayList<>();

                        if (s != null) { //As long as person has a spouse draw lines otherwise assume its null
                            for (Event f : data.getEvents()) {
                                if (f.getPersonID().equals(s.getPersonID())) {
                                    lineEvents.add(f);
                                }
                            }

                            for (Event d : lineEvents) {
                                if (j == null) {
                                    j = d;
                                }
                                if (j.getYear() > d.getYear()) {
                                    j = d;
                                }
                            }
                            if (j != null) {
                                LatLng spouse = new LatLng(j.getLatitude(), j.getLongitude());
                                Polyline p = map.addPolyline(new PolylineOptions()
                                        .add(sydney, spouse)
                                        .width(10)
                                        .color(Color.BLACK));

                                polylines.add(p);
                            }
                            lineEvents.clear();
                        }
                    }
                }
                if (g != null && data.getEventLines()) {
                    //Life Story Lines
                    ArrayList<Event> lineEvents2 = new ArrayList<>();
                    for (Event f : data.getEvents()) {

                        if (f.getPersonID().equals(g.getPersonID())) {
                            lineEvents2.add(f);
                        }
                    }
                    Comparator<Event> EventSorter = Comparator.comparingInt(Event::getYear);
                    lineEvents2.sort(EventSorter);
                    for (int i = 0; i < lineEvents2.size() - 1; ++i) {
                        LatLng current = new LatLng(lineEvents2.get(i).getLatitude(), lineEvents2.get(i).getLongitude());
                        LatLng next = new LatLng(lineEvents2.get(i + 1).getLatitude(), lineEvents2.get(i + 1).getLongitude());
                        Polyline p = map.addPolyline(new PolylineOptions()
                                .add(current, next)
                                .width(8)
                                .color(Color.YELLOW));
                        polylines.add(p);
                    }
                    lineEvents2.clear();
                }


                ImageView genderImageView = requireView().findViewById(R.id.genderImage);
                Drawable genderIcon;
                assert g != null;
                String gender = g.getGender().toUpperCase();

                switch (gender) {
                    case "M":
                        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                                .colorRes(R.color.black)
                                .sizeDp(40);
                        genderImageView.setImageDrawable(genderIcon);
                        break;
                    case "F":
                        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                                .colorRes(R.color.black)
                                .sizeDp(40);
                        genderImageView.setImageDrawable(genderIcon);
                        break;
                }


                textview.setText(new StringBuilder().append(g.getFirstName())
                                                    .append(" ")
                                                    .append(g.getLastName())
                                                    .append("\n")
                                                    .append(e.getEventType())
                                                    .append(": ")
                                                    .append(e.getCity())
                                                    .append(", ")
                                                    .append(e.getCountry())
                                                    .append(", ")
                                                    .append(e.getYear()).toString());
            }
        }

        map.setOnMarkerClickListener(m -> {
            for(Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();
            TextView textview = view.findViewById(R.id.mapTextView);
            Event e = (Event) m.getTag();
            Person g = null;
            for (Person p :data.getPeople()) {
                assert e != null;
                if (p.getPersonID().equals(e.getPersonID())){
                    g = p;
                }
            }
            if (g != null) {

                String gender = g.getGender();
                ImageView genderImageView = requireView().findViewById(R.id.genderImage);
                Drawable genderIcon;
                gender = gender.toUpperCase();

                switch (gender) {
                    case "M":
                        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male)
                                .colorRes(R.color.black)
                                .sizeDp(40);
                        genderImageView.setImageDrawable(genderIcon);
                        break;
                    case "F":
                        genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female)
                                .colorRes(R.color.black)
                                .sizeDp(40);
                        genderImageView.setImageDrawable(genderIcon);
                        break;
                }

                e.setEventType(e.getEventType().toUpperCase(Locale.ROOT));

                textview.setText(new StringBuilder().append(g.getFirstName())
                                                    .append(" ")
                                                    .append(g.getLastName())
                                                    .append("\n")
                                                    .append(e.getEventType())
                                                    .append(": ")
                                                    .append(e.getCity())
                                                    .append(", ")
                                                    .append(e.getCountry())
                                                    .append(", ")
                                                    .append(e.getYear()).toString());

                Person finalG = g;
                LatLng sydney = new LatLng(e.getLatitude(), e.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(sydney));
                textview.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), PersonActivity.class);
                    intent.putExtra("THIS_PERSON", finalG.getPersonID());
                    startActivity(intent);
                });
            }
            if (data.getSpouseLines()) {
                //Spouse Line
                if (g != null && (g.getSpouseID() != null)){
                    LatLng sydney = new LatLng(e.getLatitude(), e.getLongitude());
                    Person s = null;
                    for (Person p : data.getPeople()) {
                        if (g.getSpouseID().equals(p.getPersonID())) {
                            s = p;
                        }
                    }
                    Event j = null;
                    ArrayList<Event> events1 = new ArrayList<>();

                    if (s != null) { //As long as person has a spouse draw lines otherwise assume its null
                        for (Event f : data.getEvents()) {
                            if (f.getPersonID().equals(s.getPersonID())) {
                                events1.add(f);
                            }
                        }

                        for (Event d : events1) {
                            if (j == null) {
                                j = d;
                            }
                            if (j.getYear() > d.getYear()) {
                                j = d;
                            }
                        }
                        if (j != null) {
                            LatLng spouse = new LatLng(j.getLatitude(), j.getLongitude());
                            Polyline p = map.addPolyline(new PolylineOptions()
                                    .add(sydney, spouse)
                                    .width(10)
                                    .color(Color.BLACK));

                            polylines.add(p);
                        }
                        events1.clear();
                    }
                }
            }
            if (g != null && data.getEventLines()) {
                //Life Story Lines
                ArrayList<Event> events1 = new ArrayList<>();
                for (Event f : data.getEvents()) {

                    if (f.getPersonID().equals(g.getPersonID())) {
                        events1.add(f);
                    }
                }
                Comparator<Event> EventSorter = Comparator.comparingInt(Event::getYear);
                events1.sort(EventSorter);
                for (int i = 0; i < events1.size() - 1; ++i) {
                    LatLng current = new LatLng(events1.get(i).getLatitude(), events1.get(i).getLongitude());
                    LatLng next = new LatLng(events1.get(i + 1).getLatitude(), events1.get(i + 1).getLongitude());
                    Polyline p = map.addPolyline(new PolylineOptions()
                            .add(current, next)
                            .width(8)
                            .color(Color.YELLOW));
                    polylines.add(p);
                }
                events1.clear();
            }
            if (data.getFamilyTreeLines()) {
                //family tree lines
                familyTreeLineHelper(g, e, 16);
            }
            return true;
        });
    }

    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void familyTreeLineHelper(Person g, Event e, int num){
        if (num <= 0){
            num = 1;
        }
        DataCache data = DataCache.getInstance();
        if (g.getFatherID() != null) {
            Person f = null;
            for (Person p : data.getPeople()) {
                if (g.getFatherID().equals(p.getPersonID())) {
                    f = p;
                }
            }

            Event j = null;
            ArrayList<Event> events = new ArrayList<>();
            for (Event d : data.getEvents()) {
                if (f != null && d.getPersonID().equals(f.getPersonID())) {
                    events.add(d);
                }
            }
            if (events.size() > 0) {
                for (Event event : events) {
                    if (event.getEventType().equals("birth")) {
                        j = event;
                    }
                }
                if (j == null) {
                    Comparator<Event> EventSorter = Comparator.comparingInt(Event::getYear);
                    events.sort(EventSorter);
                    j = events.get(0);
                }

                LatLng current = new LatLng(e.getLatitude(), e.getLongitude());
                LatLng next = new LatLng(j.getLatitude(), j.getLongitude());
                Polyline p = map.addPolyline(new PolylineOptions()
                        .add(current, next)
                        .width(num)
                        .color(Color.BLUE));
                polylines.add(p);
                familyTreeLineHelper(f, j, num - 4);
            }
        }
        if (g.getMotherID() != null) {
            Person m = null;
            for (Person p : data.getPeople()) {
                if (g.getMotherID().equals(p.getPersonID())) {
                    m = p;
                }
            }
            Event j = null;
            ArrayList<Event> events = new ArrayList<>();
            for (Event d : data.getEvents()) {
                if (m != null && d.getPersonID().equals(m.getPersonID())) {
                    events.add(d);
                }
            }
            for (Event event : events){
                if (event.getEventType().equals("birth")){
                    j = event;
                }
            }
            if (events.size() > 0) {
                if (j == null) {
                    Comparator<Event> EventSorter = Comparator.comparingInt(Event::getYear);
                    events.sort(EventSorter);
                    j = events.get(0);
                }
                LatLng current = new LatLng(e.getLatitude(), e.getLongitude());
                LatLng next = new LatLng(j.getLatitude(), j.getLongitude());
                Polyline p = map.addPolyline(new PolylineOptions()
                        .add(current, next)
                        .width(num)
                        .color(Color.MAGENTA));
                polylines.add(p);
                familyTreeLineHelper(m, j, num - 4);
            }
        }
    }


}