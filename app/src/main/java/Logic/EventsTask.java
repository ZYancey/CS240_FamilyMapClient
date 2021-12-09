package Logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Network.DataCache;
import Network.ServerProxy;
import modelClass.Event;

import result.EventResult;

public class EventsTask extends AsyncTask<String, Integer, EventResult> {
    @SuppressLint("StaticFieldLeak")
    private final Context mcontext;
    interface EventsTaskListener {}
    public EventsTask(Context context){
        mcontext = context;
    }
    private final List<EventsTaskListener> listeners = new ArrayList<>();


    void EventsListener(EventsTaskListener listener) {
        listeners.add(listener);
    }


    @Override
    protected EventResult doInBackground(String... authToken) {
        EventResult EventsResult = new EventResult("EXAMPLE STRING");
        ServerProxy serverProxy = new ServerProxy();
        try {
            EventsResult = serverProxy.getEvents(authToken[0]);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        return EventsResult;
    }



    @Override
    protected void onPostExecute(EventResult result) {
        if (result.getMessage().contains("val")) {
            DataCache data = DataCache.getInstance();
            Event[] resultData = result.getData();

            ArrayList<Event> tempList = new ArrayList<>();
            Collections.addAll(tempList, resultData);


            data.setOriginalEvents(tempList);
        }
    }
}
