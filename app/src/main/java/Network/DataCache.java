package Network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import modelClass.AuthToken;
import modelClass.Event;
import modelClass.Person;

public class DataCache {
    private static DataCache instance;

    private AuthToken authToken;
    private String serverPort;
    private String serverHost;
    private Person user;
    private String userFirst;
    private String userLast;
    private String username;
    private String userPersonID;
    private ArrayList<Person> People = new ArrayList<>();
    private ArrayList<Event>OriginalEvents = new ArrayList<>();
    private ArrayList<Event>Events = new ArrayList<>();
    private ArrayList<Person> MothersSidePeople = new ArrayList<>();
    private ArrayList<Person> FathersSidePeople = new ArrayList<>();
    private ArrayList<Person> MothersSide = new ArrayList<>();
    private ArrayList<Person> FathersSide = new ArrayList<>();
    private boolean lifeStoryLines = true;
    private boolean familyTreeLines = true;
    private boolean spouseLines = true;
    private boolean fathersSide = true;
    private boolean mothersSide = true;
    private boolean maleEvents = true;
    private boolean femaleEvents = true;

    public boolean getLifeStoryLines() {
        return lifeStoryLines;
    }

    public ArrayList<Event> getOriginalEvents() {
        return OriginalEvents;
    }

    public void setOriginalEvents(ArrayList<Event> originalEvents) {
        OriginalEvents = originalEvents;

        MothersSidePeople = setMothersSidePeople();
        FathersSidePeople = setFathersSidePeople();
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean getFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean getSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean getFathersSide() {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }

    public boolean getMothersSide() {
        return mothersSide;
    }

    public void setMothersSide(boolean mothersSide) {
        this.mothersSide = mothersSide;
    }

    public boolean getMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean getFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public ArrayList<Event> getUserEvents(){
        ArrayList<Event> userEvents = new ArrayList<>() ;
        for (Event e :OriginalEvents){
            if (e.getPersonID().equals(user.getPersonID())){
                userEvents.add(e);
            }
        }
        return userEvents;
    }


    public ArrayList<Event> setFatherEvents() {
        ArrayList<Event> FEvents = new ArrayList<>();
        for (Event e :OriginalEvents){
            for (Person p : FathersSidePeople) {
                if (e.getPersonID().equals(p.getPersonID())){
                    FEvents.add(e);
                }
            }
        }
        return FEvents;
    }
    public ArrayList<Event> setMotherEvents() {
        ArrayList<Event> MEvents = new ArrayList<>();
        for (Event e :OriginalEvents){
            for (Person p : MothersSidePeople) {
                if (e.getPersonID().equals(p.getPersonID())){
                    MEvents.add(e);
                }
            }
        }
        return MEvents;
    }
    private ArrayList<Person> setMothersSidePeople(){
        if (getUser() != null) {
            getMFamilySide(getUser().getMotherID());
        }
        return MothersSide;
    }

    private void getMFamilySide(String personID){
        Person p = null;
        for (Person c : People){
            if (c.getPersonID().equals(personID)){
                p = c;
            }
        }
        if (p != null) {
            for (Person k : People) {
                if (p.getPersonID().equals(k.getPersonID())) {
                    MothersSide.add(p);
                    getMFamilySide(p.getFatherID());
                    getMFamilySide(p.getMotherID());
                }
            }
        }
    }
    private ArrayList<Person> setFathersSidePeople(){
        getFFamilySide(getUser().getFatherID());
        return FathersSide;
    }
    private void getFFamilySide(String personID){
        Person p = null;
        for (Person c : People){
            if (c.getPersonID().equals(personID)){
                p = c;
            }
        }
        if (p != null) {
            for (Person k : People) {
                if (p.getPersonID().equals(k.getPersonID())) {
                    FathersSide.add(p);
                    getFFamilySide(p.getFatherID());
                    getFFamilySide(p.getMotherID());
                }
            }
        }
    }
    public ArrayList<Event> setMaleEvents(ArrayList<Event> events){
        ArrayList<Event> Maleevents = new ArrayList<>();
        for (Event e : events){
            for (Person p : People){
                if (p.getPersonID().equals(e.getPersonID())){
                    if (p.getGender().equals("m")){
                        Maleevents.add(e);
                    }
                }
            }
        }
        return Maleevents;
    }
    public ArrayList<Event> setFemaleEvents(ArrayList<Event> events){
        ArrayList<Event> Femaleevents = new ArrayList<>();
        for (Event e : events){
            for (Person p : People){
                if (p.getPersonID().equals(e.getPersonID())){
                    if (p.getGender().equals("f")){
                        Femaleevents.add(e);
                    }
                }
            }
        }
        return Femaleevents;
    }

    public Person getUser() {
        return user;
    }

    public ArrayList<Event> getEvents() {
        return Events;
    }

    public void setEvents(ArrayList<Event> events) {
        Events = events;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public void setUserFirst(String userFirst){ this.userFirst = userFirst;}
    public void setUserLast(String userLast){ this.userLast = userLast;}

    public String getUserFirstandLast(){
        return userFirst+" "+userLast;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username, String PersonID) {
        this.username = username;
        this.userPersonID = PersonID;
    }

    public String getUserPersonID(){
        return userPersonID;
    }

    public ArrayList<Person> getPeople() {
        return People;
    }

    public void setPeople(ArrayList<Person> people) {
        People = people;

        for (Person p : people){
            if (userPersonID.equals(p.getPersonID())){
                user = p;
            }
        }
    }


    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public static DataCache getInstance(){
        if (instance == null){
            instance = new DataCache();
        }
        return instance;
    }
}