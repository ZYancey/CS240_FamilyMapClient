package com.example.familymapclient.UI;

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

class RegisterTask extends AsyncTask<RegisterRequest, Integer, AuthResult> {
private Context mcontext;
interface RegisterTaskListener {
    void progressUpdated(int progress);
    void taskCompleted(long result);
}
    public RegisterTask(Context context){
        mcontext = context;
    }
    private final List<RegisterTaskListener> listeners = new ArrayList<>();


    void registerListener(RegisterTaskListener listener) {
        listeners.add(listener);
    }

    private void fireProgressUpdate(int progress) {
        for(RegisterTaskListener listener : listeners) {
            listener.progressUpdated(progress);
        }
    }

    private void fireTaskCompleted(long result) {
        for(RegisterTaskListener listener : listeners) {
            listener.taskCompleted(result);
        }
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
        //if (result.getSuccess() == false) {
        if (!result.getMessage().contains("val")) {
            Toast.makeText(mcontext, "Register Failed",Toast.LENGTH_LONG).show();
        }
        else {
            DataCache data = DataCache.getInstance();

            String FN = data.getUserFirstandLast();


            Toast.makeText(mcontext, FN, Toast.LENGTH_LONG).show();


        }
    }
}
