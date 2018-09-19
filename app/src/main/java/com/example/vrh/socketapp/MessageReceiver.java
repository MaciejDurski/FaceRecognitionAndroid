package com.example.vrh.socketapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

class MessageReceiver extends AsyncTask<Void,Void,Void>{
    Context context;

    String msg;

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(context,"welcome back",Toast.LENGTH_LONG).show();
        if(msg.equals("Not recognized")){
            Intent intent = new Intent(context,NotRecognizedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(context,WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }


    }

    public MessageReceiver(Context context){
        this.context=context.getApplicationContext();
    }


    @Override
    protected Void doInBackground(Void... voids) {

        Log.e("hello","messageReceiver");


        Socket socket = null;
        try {
            socket = new Socket("10.7.54.13",839);
            DataInputStream dis= new DataInputStream(socket.getInputStream());

            msg = dis.readUTF();
            System.out.println(msg);
            Log.e("najs",msg);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}
