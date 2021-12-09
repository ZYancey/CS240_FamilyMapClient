package Logic;
import com.example.FamilyMapClient.UI.MainActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Network.ServerProxy;
import request.LoginRequest;
import result.AuthResult;

import Network.DataCache;


public class LoginTask extends AsyncTask<LoginRequest, Integer, AuthResult>{
    @SuppressLint("StaticFieldLeak")
    private final Context mcontext;
    interface LoginTaskListener {}

    public LoginTask(Context context){
        mcontext = context;
    }
    private final List<LoginTaskListener> listeners = new ArrayList<>();


    void registerListener(LoginTaskListener listener) {
        listeners.add(listener);
    }

    @Override
    protected AuthResult doInBackground(LoginRequest... r) {
        AuthResult loginResult = new AuthResult("nullSTRING");
        ServerProxy serverProxy = new ServerProxy();

        DataCache data = DataCache.getInstance();

        try {
            loginResult = serverProxy.runLogin(r[0]);
            serverProxy.getUserPersonData(data.getAuthToken());

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return loginResult;
    }



    @Override
    protected void onPostExecute(AuthResult result) {

        if (!result.getMessage().contains("val")) {
            Toast.makeText(mcontext, "Login Failed",Toast.LENGTH_LONG).show();
        }
        else {
            DataCache data = DataCache.getInstance();
            String FN = data.getUserFullName();
            Toast.makeText(mcontext, FN, Toast.LENGTH_LONG).show();


            PeopleTask task = new PeopleTask(mcontext);
            task.execute(data.getAuthToken().getAuthTokenID());
            EventsTask tasks = new EventsTask(mcontext);
            tasks.execute(data.getAuthToken().getAuthTokenID());


            MainActivity.getInstance().switchToMap(data.getUsername());

        }
    }
}
