package Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Network.DataCache;
import Network.ServerProxy;
import modelClass.Person;


import result.PersonResult;

@SuppressLint("StaticFieldLeak")
public class PeopleTask extends AsyncTask<String, Integer, PersonResult> {
    private final Context mcontext;
    interface PeopleTaskListener {}
    public PeopleTask(Context context){
        mcontext = context;
    }
    private final List<PeopleTaskListener> listeners = new ArrayList<>();


    void PeopleListener(PeopleTaskListener listener) {
        listeners.add(listener);
    }

    @Override
    protected PersonResult doInBackground(String... authToken) {
        PersonResult PeopleResult = new PersonResult("test");
        ServerProxy serverProxy = new ServerProxy();
        try {
            PeopleResult = serverProxy.getPersons(authToken[0]);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return PeopleResult;
    }



    @Override
    protected void onPostExecute(PersonResult result) {
        if (result.getMessage().contains("val")) {
            DataCache data = DataCache.getInstance();
            Person[] R = result.getData();

            ArrayList<Person> list1 = new ArrayList<>();
            Collections.addAll(list1, R);

            data.setPeople(list1);

            Toast.makeText(mcontext, data.getUser().getFirstName() + " " + data.getUser().getLastName(), Toast.LENGTH_LONG).show();
        }
    }
}
