package Network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import modelClass.AuthToken;
import modelClass.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.AuthResult;
import result.PersonResult;
//import result.RegisterResult;

public class ServerProxy {

    private static final String LOG_TAG = "ServerProxy";
    private static DataCache data = DataCache.getInstance();
    private static String host = data.getServerHost();
    private static String port = data.getServerPort();

    public AuthResult runLogin(LoginRequest r) throws MalformedURLException, IOException {
        URL url = new URL("http://" + host + ":" + port + "/user/login");
        System.out.println(url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();
        try(OutputStream requestBody = connection.getOutputStream();){
            String json = JSONParse.serialize(r);
            OutputStreamWriter osw = new OutputStreamWriter(requestBody, "UTF-8");
            osw.write(json);
            osw.close();
            requestBody.close();
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

            AuthToken L = (AuthToken) JSONParse.deserialize(buf.toString(), AuthToken.class);

            DataCache data = DataCache.getInstance();

            data.setUsername(L.getUserName(), L.getPersonID());
            data.setAuthToken(L);
            AuthResult LRes = new AuthResult(L);
            LRes.setMessage("valid");
            return LRes;

            //++++++

            //AuthResult L = (AuthResult) JSONParse.deserialize(buf.toString(), AuthResult.class);
            //DataCache data = DataCache.getInstance();
            //data.setUsername(L.getAuthToken().getUserName(), L.getAuthToken().getPersonID());
            //return L;
        }
        else {
            AuthResult L = new AuthResult("Failed to receive Result");
            //L.setMessage("Failed to receive Result");
            //L.setSuccess(false);
            return L;
        }

    }


    public AuthResult runRegister(RegisterRequest r) throws MalformedURLException, IOException {
        URL url = new URL("http://" + host + ":" + port + "/user/register");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.connect();
        try(OutputStream requestBody = connection.getOutputStream();){
            String json = JSONParse.serialize(r);
            OutputStreamWriter osw = new OutputStreamWriter(requestBody, "UTF-8");
            osw.write(json);
            osw.close();
            requestBody.close();
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

            AuthToken L = (AuthToken) JSONParse.deserialize(buf.toString(), AuthToken.class);

            DataCache data = DataCache.getInstance();

            data.setUsername(L.getUserName(), L.getPersonID());
            data.setAuthToken(L);
            AuthResult LRes = new AuthResult(L);
            LRes.setMessage("valid");
            return LRes;
            //return LResult;
        }
        else {
            AuthResult L = new AuthResult("Failed to receive Result");
            //L.setMessage("Failed to receive Result");
            //L.setSuccess(false);
            return L;
        }

    }

    public PersonResult getUserPersonData(AuthToken authToken) throws MalformedURLException, IOException {

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
            return L;
        }
        else{
            PersonResult L = new PersonResult("Failed to receive Result");
            //L.setMessage("Failed to receive Result");
            //L.setSuccess(false);
            return L;
        }
    }


    /*
    public PeopleResult runPeople(String authToken) throws MalformedURLException, IOException {
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
            PeopleResult L = JSONParse.deserialize(buf.toString(), PeopleResult.class);
            return L;
        }
        else {
            PeopleResult L = new PeopleResult();
            L.setMessage("Failed to receive Result");
            L.setSuccess(false);
            return L;
        }

    }

    public EventsResult runEvents(String authToken) throws MalformedURLException, IOException {
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
            EventsResult L = JSONParse.deserialize(buf.toString(), EventsResult.class);
            return L;
        }
        else {
            EventsResult L = new EventsResult();
            L.setMessage("Failed to receive Result");
            L.setSuccess(false);
            return L;
        }

    }

*/

    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

}