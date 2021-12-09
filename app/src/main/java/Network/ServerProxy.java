package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;

import modelClass.AuthToken;
import modelClass.Event;
import modelClass.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.AuthResult;
import result.PersonResult;


public class ServerProxy {
    private static final DataCache data = DataCache.getInstance();
    private static final String host = data.getFMSHost();
    private static final String port = data.getFMSPort();

    public AuthResult runLogin(LoginRequest r) throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/user/login");
        System.out.println(url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();
        try(OutputStream requestBody = connection.getOutputStream()){
            String json = JSONParse.serialize(r);
            OutputStreamWriter osw = new OutputStreamWriter(requestBody, StandardCharsets.UTF_8);
            osw.write(json);
            osw.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            AuthToken loginToken = (AuthToken) JSONParse.deserialize(buf.toString(), AuthToken.class);

            DataCache data = DataCache.getInstance();

            data.refreshDataCache();

            data.setUsername(loginToken.getUserName(), loginToken.getPersonID());
            data.setAuthToken(loginToken);
            AuthResult LRes = new AuthResult(loginToken);
            LRes.setMessage("valid");
            return LRes;
        }
        else {
            return new AuthResult("Failed to receive Result");
        }

    }

    public AuthResult runRegister(RegisterRequest r) throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/user/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();
        try(OutputStream requestBody = connection.getOutputStream()){
            String json = JSONParse.serialize(r);
            OutputStreamWriter osw = new OutputStreamWriter(requestBody, StandardCharsets.UTF_8);
            osw.write(json);
            osw.close();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            AuthToken loginToken = (AuthToken) JSONParse.deserialize(buf.toString(), AuthToken.class);

            DataCache data = DataCache.getInstance();
            data.refreshDataCache();

            data.setUsername(loginToken.getUserName(), loginToken.getPersonID());
            data.setAuthToken(loginToken);
            AuthResult LRes = new AuthResult(loginToken);
            LRes.setMessage("valid");
            return LRes;
        }
        else {
            return new AuthResult("Failed to receive Result");
        }

    }


    public void getUserPersonData(AuthToken authToken) throws IOException {
        URL url = new URL("http://" + host + ":" + port+ "/person/" + authToken.getPersonID());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Authorization", authToken.getAuthTokenID());
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            Person P = (Person) JSONParse.deserialize(buf.toString(), Person.class);
            data.setUser(P);
            PersonResult L = new PersonResult(P);
            L.setMessage("valid");
        }
        else{
            new PersonResult("Failed to receive Result");
        }
    }

    public PersonResult getPersons(String authToken) throws IOException {
        URL url = new URL("http://" + host + ":" + port+ "/person");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Authorization", authToken);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            PersonResult K = (PersonResult) JSONParse.deserialize(buf.toString(), PersonResult.class);

            Person[] P = K.getData();

            ArrayList<Person> list1 = new ArrayList<>();
            Collections.addAll(list1, P);


            data.setPeople(list1);

            K.setMessage("Valid");
            return K;
        }
        else {
            PersonResult L = new PersonResult("Failed to receive result");
            L.setMessage("Failed to receive Result");
            return L;
        }

    }

    public EventResult getEvents(String authToken) throws IOException {
        URL url = new URL("http://" + host + ":" + port + "/event");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.addRequestProperty("Authorization", authToken);
        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream responseBody = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(responseBody, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            EventResult K = (EventResult) JSONParse.deserialize(buf.toString(), EventResult.class);

            Event[] E = K.getData();

            ArrayList<Event> list1 = new ArrayList<>();
            Collections.addAll(list1, E);

            data.setOriginalEvents(list1);

            K.setMessage("Valid");
            return K;
        }
        else {
            EventResult L = new EventResult("Failed to receive result");
            L.setMessage("Failed to receive Result");
            return L;
        }
    }


}