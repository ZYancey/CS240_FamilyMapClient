package Network;

import java.util.ArrayList;

import modelClass.AuthToken;
import modelClass.Event;
import modelClass.Person;

public class DataCache {
    private static DataCache    instance;
    private AuthToken           authToken;
    private String              FMSPort;
    private String              FMSHost;

    private Person              user;
    private String              userFirst;
    private String              userLast;
    private String              username;
    private String              userPersonID;

    private ArrayList<Person>   People = new ArrayList<>();
    private ArrayList<Event>    OriginalEvents = new ArrayList<>();
    private ArrayList<Event>    Events = new ArrayList<>();
    private ArrayList<Person>   MothersSidePeople = new ArrayList<>();
    private ArrayList<Person>   FathersSidePeople = new ArrayList<>();
    private final ArrayList<Person>   MothersSide = new ArrayList<>();
    private final ArrayList<Person>   FathersSide = new ArrayList<>();

    private boolean             eventLines = true;
    private boolean             familyTreeLines = true;
    private boolean             spouseLines = true;
    private boolean             fathersSide = true;
    private boolean             mothersSide = true;
    private boolean             maleEvents = true;
    private boolean             femaleEvents = true;



    //=================SETTERS==========================

    public void setOriginalEvents(ArrayList<Event> originalEvents) {
        OriginalEvents = originalEvents;

        MothersSidePeople = setMothersSidePeople();
        FathersSidePeople = setFathersSidePeople();
    }
    public void setEventLines(boolean eventLines) {
        this.eventLines = eventLines;
    }
    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }
    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }
    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }
    public void setMothersSide(boolean mothersSide) {
        this.mothersSide = mothersSide;
    }
    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }
    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
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

    private ArrayList<Person> setFathersSidePeople(){
        getFemaleFamilySide(getUser().getFatherID());
        return FathersSide;
    }
    private ArrayList<Person> setMothersSidePeople(){
        if (getUser() != null) {
            getMaleFamilySide(getUser().getMotherID());
        }
        return MothersSide;
    }

    public ArrayList<Event> setMaleEvents(ArrayList<Event> events){
        ArrayList<Event> Maleevents = new ArrayList<>();
        for (Event e : events){
            for (Person p : People){
                if (p.getPersonID().equals(e.getPersonID())){
                    if (p.getGender().equals("m") || p.getGender().equals("M")){
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
                    if (p.getGender().equals("f") || p.getGender().equals("F")){
                        Femaleevents.add(e);
                    }
                }
            }
        }
        return Femaleevents;
    }


    public void setUser(Person user) {
        this.user = user;
        setUserFirst(user.getFirstName());
        setUserLast(user.getLastName());
    }
    public void setEvents(ArrayList<Event> events) {
        Events = events;
    }
    public void setUserFirst(String userFirst){ this.userFirst = userFirst;}
    public void setUserLast(String userLast){ this.userLast = userLast;}
    public void setUsername(String username, String PersonID) {
        this.username = username;
        this.userPersonID = PersonID;
    }

    public void setPeople(ArrayList<Person> people) {
        People = people;

        for (Person p : people){
            if (userPersonID.equals(p.getPersonID())){
                user = p;
            }
        }
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
    public void setFMSPort(String FMSPort) {
        this.FMSPort = FMSPort;
    }
    public void setFMSHost(String FMSHost) {
        this.FMSHost = FMSHost;
    }

    //=================GETTERS==========================
    public AuthToken getAuthToken() {
        return authToken;
    }
    public String getFMSPort() {
        return FMSPort;
    }
    public String getFMSHost() {
        return FMSHost;
    }

    public static DataCache getInstance(){
        if (instance == null){
            instance = new DataCache();
        }
        return instance;
    }

    public boolean getEventLines() {
        return eventLines;
    }

    public boolean getFamilyTreeLines() {
        return familyTreeLines;
    }
    public boolean getSpouseLines() {
        return spouseLines;
    }
    public boolean getFathersSide() {
        return fathersSide;
    }
    public boolean getMothersSide() {
        return mothersSide;
    }
    public boolean getMaleEvents() {
        return maleEvents;
    }
    public boolean getFemaleEvents() {
        return femaleEvents;
    }

    public ArrayList<Event> getUserEvents(){
        ArrayList<Event> userEvents = new ArrayList<>() ;
        for (Event e :OriginalEvents){
            if (e.getPersonID().equals(user.getPersonID())||e.getPersonID().equals(user.getSpouseID())){
                userEvents.add(e);
            }
        }
        return userEvents;
    }

    private void getMaleFamilySide(String personID){
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
                    getMaleFamilySide(p.getFatherID());
                    getMaleFamilySide(p.getMotherID());
                }
            }
        }
    }
    private void getFemaleFamilySide(String personID){
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
                    getFemaleFamilySide(p.getFatherID());
                    getFemaleFamilySide(p.getMotherID());
                }
            }
        }
    }

    public Person getUser() {
        return user;
    }
    public ArrayList<Event> getEvents() {
        return Events;
    }
    public String getUserFullName(){
        return userFirst+" "+userLast;
    }
    public String getUsername() {
        return username;
    }

    public ArrayList<Person> getPeople() {
        return People;
    }



    public void refreshDataCache() { //I know I can create and call multiple instances but this seemed easier
        eventLines = true;
        familyTreeLines = true;
        spouseLines = true;
        fathersSide = true;
        mothersSide = true;
        maleEvents = true;
        femaleEvents = true;

        user = null;
        userFirst = null;
        userLast = null;
        username = null;
        userPersonID = null;

        People.clear();
        OriginalEvents.clear();
        Events.clear();
        MothersSidePeople.clear();
        FathersSidePeople.clear();
        MothersSide.clear();
        FathersSide.clear();
    }
}