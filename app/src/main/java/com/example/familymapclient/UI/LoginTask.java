package com.example.familymapclient.UI;
import com.example.familymapclient.R;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import Network.ServerProxy;
import modelClass.AuthToken;
import request.LoginRequest;
import result.AuthResult;

import Network.DataCache;
import Network.ServerProxy;
import result.PersonResult;

import static android.app.PendingIntent.getActivity;

class LoginTask extends AsyncTask<LoginRequest, Integer, AuthResult>{
    private Context mcontext;
    interface LoginTaskListener {
        void progressUpdated(int progress);
        void taskCompleted(long result);
    }

    public LoginTask(Context context){
        mcontext = context;
    }
    private final List<LoginTaskListener> listeners = new ArrayList<>();


    void registerListener(LoginTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(LoginTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(LoginTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
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

            String FN = data.getUser().getFirstName();
            String LN = data.getUser().getLastName();



            Toast.makeText(mcontext, FN+" "+LN, Toast.LENGTH_LONG).show();


        }
    }
}
