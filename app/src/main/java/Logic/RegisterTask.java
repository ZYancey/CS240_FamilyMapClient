package Logic;
import com.example.FamilyMapClient.UI.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Network.DataCache;
import Network.ServerProxy;
import request.RegisterRequest;
import result.AuthResult;

public class RegisterTask extends AsyncTask<RegisterRequest, Integer, AuthResult> {
@SuppressLint("StaticFieldLeak")
private final Context mcontext;
interface RegisterTaskListener { }
    public RegisterTask(Context context){
        mcontext = context;
    }
    private final List<RegisterTaskListener> listeners = new ArrayList<>();


    void registerListener(RegisterTaskListener listener) {
        listeners.add(listener);
    }

    @Override
    protected AuthResult doInBackground(RegisterRequest... r) {
        AuthResult RegisterResult = new AuthResult("nullSTRING");
        ServerProxy serverProxy = new ServerProxy();
        try {
            RegisterResult = serverProxy.runRegister(r[0]);

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return RegisterResult;
    }



    @Override
    protected void onPostExecute(AuthResult result) {
        if (!result.getMessage().contains("val")) {
            Toast.makeText(mcontext, "Register Failed",Toast.LENGTH_LONG).show();
        }
        else {
            DataCache data = DataCache.getInstance();

            PeopleTask task = new PeopleTask(mcontext);
            task.execute(data.getAuthToken().getAuthTokenID());
            EventsTask tasks = new EventsTask(mcontext);
            tasks.execute(data.getAuthToken().getAuthTokenID());

            MainActivity.getInstance().switchToMap(data.getUsername());
        }
    }
}
