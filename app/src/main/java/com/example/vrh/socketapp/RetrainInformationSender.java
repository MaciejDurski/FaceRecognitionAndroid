package com.example.vrh.socketapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RetrainInformationSender extends AsyncTask<String,Void,Void>{
    Context context;
    String name;
    PrintWriter printWriter;

    public RetrainInformationSender(Context context){
        this.context=context.getApplicationContext();
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Intent intent = new Intent(context,WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected Void doInBackground(String... strings) {
        name=strings[0];

        try{

        Socket socket = new Socket("10.7.54.13", 1200);
        printWriter = new PrintWriter(socket.getOutputStream());

        printWriter.write(name);
        printWriter.flush();
        printWriter.close();
        socket.close();
    }catch (IOException e){
        e.printStackTrace();
    }

        return null;
    }
}
