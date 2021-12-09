package com.example.FamilyMapClient;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import Network.DataCache;
import Network.ServerProxy;
import modelClass.Event;
import modelClass.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.AuthResult;
import result.PersonResult;


import static org.junit.Assert.*;


public class FamilyMapClientJUnitTests {
    ServerProxy sp;
    DataCache data = DataCache.getInstance();
    AuthResult e;
    @Before
    public void setUp(){
        data.setFMSHost("localhost");
        data.setFMSPort("8080");
        sp = new ServerProxy();
    }
    @Before
    public void RunRegister() throws IOException{
        RegisterRequest r = new RegisterRequest("Joey", "Schmitty", "js@byu.edu", "Joseph", "Smith","M", "LDS_Prophet");
        sp.runRegister(r);
        LoginRequest g = new LoginRequest("Joey", "Schmitty");
        e = sp.runLogin(g);
        sp.getUserPersonData(e.getAuthToken());
    }
    @Test
    public void S_RunRegisterFail() throws IOException{
        RegisterRequest r = new RegisterRequest("Joey", "Schmitty", "js@byu.edu", "Joseph", "Smith","M", "LDS_Prophet");
        sp.runRegister(r);
        AuthResult f = sp.runRegister(r);
        assertFalse(f.getMessage().contains("val"));
    }
    @Test
    public void S_RunRegisterPass() throws IOException{
        RegisterRequest r = new RegisterRequest("USER", "PASS", "js@byu.edu", "Joseph", "Smith","M", "LDS_Prophet");
        sp.runRegister(r);
        AuthResult f = sp.runRegister(r);
        assertTrue(f.getMessage().contains("u"));
    }
    @Test
    public void S_RunLoginPass() throws IOException {
        LoginRequest g = new LoginRequest("Joey", "Schmitty");
        AuthResult f = sp.runLogin(g);
        assertEquals("Joey",f.getAuthToken().getUserName());
        assertNotNull(f.getAuthToken());
        assertNotNull(f.getAuthToken().getPersonID());
        assertTrue(f.getMessage().contains("val"));
    }
    @Test
    public void S_RunLoginFail_Password() throws IOException{
        LoginRequest g = new LoginRequest("Joey","G");
        AuthResult f = sp.runLogin(g);
        assertFalse(f.getMessage().contains("val"));
    }

    @Test
    public void S_RunLoginFail_Username() throws IOException{
        LoginRequest g = new LoginRequest("J","Schmitty");
        AuthResult f = sp.runLogin(g);
        assertFalse(f.getMessage().contains("val"));

    }

    @Test
    public void S_GetPersonsPass() throws IOException{
        PersonResult r = sp.getPersons(e.getAuthToken().getAuthTokenID());
        assertNotNull(r.getData());
    }
    @Test
    public void S_GetEventsPass() throws IOException{
        EventResult r = sp.getEvents(e.getAuthToken().getAuthTokenID());
        assertNotNull(r.getData());
    }

    @Test
    public void S_GetPersonsFail_BadAuth() throws IOException{
        PersonResult r = sp.getPersons("BadAuth");
        assertNull(r.getData());
    }

    @Test
    public void S_GetEventsFail_BadAuth() throws IOException{
        EventResult r = sp.getEvents("BadAuth");
        assertNull(r.getData());
    }


    @Test
    public void DC_FilterMaleEvents() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        ArrayList<Event> events = data.setMaleEvents(data.getOriginalEvents());
        assertEquals(data.getOriginalEvents().size(), events.size() * 2L - 3);
    }

    @Test
    public void DC_FilterFemaleEvents() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        ArrayList<Event> events = data.setFemaleEvents(data.getOriginalEvents());
        assertEquals(data.getOriginalEvents().size(), events.size() * 2L + 3);
    }


    @Test
    public void DC_FilterMothersSide() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        ArrayList<Event> events = data.setMotherEvents();
        assertEquals(data.getOriginalEvents().size(), events.size() * 2L + 3);
    }


    @Test
    public void DC_FilterFathersSide() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        ArrayList<Event> events = data.setFatherEvents();
        assertEquals(data.getOriginalEvents().size(), events.size() * 2L + 3);
    }

    @Test
    public void DC_ValidChronologicalSort() throws IOException {
        String FatherID = data.getUser().getFatherID();
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        Person g = data.getUser();
        Person father = null;
        if (g.getFatherID() != null) {
            for (Person person : data.getPeople()) {
                if (g.getFatherID().equals(person.getPersonID())) {
                    father = person;
                }
            }
        }
        assert father != null;
        assertEquals(father.getPersonID(), FatherID);
    }

    @Test
    public void DC_InvalidChronologicalSort() throws IOException {
        String MotherID = data.getUser().getMotherID();
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        Person g = data.getUser();
        Person father = null;
        if (g.getFatherID() != null) {
            for (Person person : data.getPeople()) {
                if (g.getFatherID().equals(person.getPersonID())) {
                    father = person;
                }
            }
        }
        assert father != null;
        assertNotEquals(father.getPersonID(), MotherID);
    }

    @Test
    public void DC_SearchWithValidQuery() throws IOException {
        String MotherID = data.getUser().getMotherID();
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        Person g = data.getUser();
        Person father = null;
        if (g.getFatherID() != null) {
            for (Person person : data.getPeople()) {
                if (g.getFatherID().equals(person.getPersonID())) {
                    father = person;
                }
            }
        }
        assert father != null;
        assertNotEquals(father.getPersonID(), MotherID);
    }

    @Test
    public void DC_SearchWithInvalidQuery() throws IOException {
        String MotherID = data.getUser().getMotherID();
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        Person g = data.getUser();
        Person father = null;
        if (g.getFatherID() != null) {
            for (Person person : data.getPeople()) {
                if (g.getFatherID().equals(person.getPersonID())) {
                    father = person;
                }
            }
        }
        assert father != null;
        assertNotEquals(father.getPersonID(), MotherID);
    }


    @Test
    public void DC_ClearDataCache() throws IOException {
        if (data.getOriginalEvents().size() == 0){
            //Fill dataCache with events
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());
            data.setOriginalEvents(list1);
        }
        data.refreshDataCache();
        assertNull(data.getUser());
    }

    @Test
    public void DC_ClearNotAffectingPersistentData() {
        data.refreshDataCache();
        assertNotNull(data.getFMSHost());
        assertNotNull(data.getFMSPort());
    }


    @Test
    public void DC_SwitchEventLinesFlag() {
        assertTrue(data.getEventLines());
        data.setEventLines(false);
        assertFalse(data.getEventLines());
    }

    @Test
    public void DC_SwitchSpouseLinesFlag() {
        assertTrue(data.getSpouseLines());
        data.setSpouseLines(false);
        assertFalse(data.getSpouseLines());
    }

    @Test
    public void DC_SwitchFamilyLinesFlag() {
        assertTrue(data.getFamilyTreeLines());
        data.setFamilyTreeLines(false);
        assertFalse(data.getFamilyTreeLines());
    }

    @Test
    public void DC_ManuallyAddData() {
        data.refreshDataCache();
        String userF = "John";
        String userL = "BYU";
        data.setUserFirst(userF);
        data.setUserLast(userL);
        assertNull(data.getUser());
        assertEquals(data.getUserFullName(), userF+" "+userL);
    }

    @Test
    public void DC_ManuallyEditData() {
        String userF = "John";
        String userL = "BYU";
        data.setUserFirst(userF);
        data.setUserLast(userL);
        assertEquals(data.getUserFullName(), userF+" "+userL);
    }

    @Test
    public void DC_ValidFamilialRelationship() throws IOException {
        String FatherID = data.getUser().getFatherID();
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        Person g = data.getUser();
        Person father = null;
        if (g.getFatherID() != null) {
            for (Person person : data.getPeople()) {
                if (g.getFatherID().equals(person.getPersonID())) {
                    father = person;
                }
            }
        }
        assert father != null;
        assertEquals(father.getPersonID(), FatherID);
    }

    @Test
    public void DC_InvalidFamilialRelationship() throws IOException {
        String MotherID = data.getUser().getMotherID();
        if (data.getOriginalEvents().size() == 0){
            EventResult m = sp.getEvents(e.getAuthToken().getAuthTokenID());
            PersonResult p = sp.getPersons(e.getAuthToken().getAuthTokenID());
            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, m.getData());

            ArrayList<Person> list2 = new ArrayList<>();
            Collections.addAll(list2, p.getData());

            data.setPeople(list2);
            data.setOriginalEvents(list1);
        }
        Person g = data.getUser();
        Person father = null;
        if (g.getFatherID() != null) {
            for (Person person : data.getPeople()) {
                if (g.getFatherID().equals(person.getPersonID())) {
                    father = person;
                }
            }
        }
        assert father != null;
        assertNotEquals(father.getPersonID(), MotherID);
    }





}