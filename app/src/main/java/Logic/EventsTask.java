package Logic;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Network.DataCache;
import Network.ServerProxy;
import modelClass.Event;
import modelClass.Person;
import request.LoginRequest;

import result.EventResult;

public class EventsTask extends AsyncTask<String, Integer, EventResult> {
    private Context mcontext;
    interface EventsTaskListener {
        void progressUpdated(int progress);
        void taskCompleted(long result);
    }
    public EventsTask(Context context){
        mcontext = context;
    }
    private final List<EventsTaskListener> listeners = new ArrayList<>();


    void EventsListener(EventsTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(EventsTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(EventsTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
    }

    @Override
    protected EventResult doInBackground(String... authToken) {
        EventResult EventsResult = new EventResult("EXAMPLE STRING");
        ServerProxy serverProxy = new ServerProxy();
        try {
            EventsResult = serverProxy.runEvents(authToken[0]);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        return EventsResult;
    }



    @Override
    protected void onPostExecute(EventResult result) {
        if (!result.getMessage().contains("val")) {
            //ttToast.makeText(mcontext, "Login Failed",Toast.LENGTH_LONG).show();
        }
        else {
            DataCache data = DataCache.getInstance();
            //TODO FIX THIS FOR LOGIN
            Event[] R = result.getData();

            ArrayList<Event> list1 = new ArrayList<Event>();
            Collections.addAll(list1, R);


            data.setOriginalEvents(list1);
        }
    }
}
