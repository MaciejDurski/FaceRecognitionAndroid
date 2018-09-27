package com.example.vrh.socketapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

class MessageReceiver extends AsyncTask<Void,Void,Void>{
    Context context;

    String msg;
    String welcome;
    float[]data;
    private ProgressDialog dialog;

    float x;
    float y;
    float z;
    String[] splitString;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       dialog.setMessage("Waiting for server response...");
        dialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

       if (dialog.isShowing()) {
            dialog.dismiss();
        }


        if(msg.equals("Not recognized")){
            Toast.makeText(context,"Not recognized",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context,NotRecognizedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if(msg.equals("face not found")){
            Toast.makeText(context,"Face not found, try again",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else{

            if((x-1<data[0]&&data[0]<x+1)&&(y-1<data[1]&&data[1]<y+1)&&(z-1<data[2]&&data[2]<z+1)) {

                Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else{
                Toast.makeText(context,"wrong accelometer data",Toast.LENGTH_LONG).show();
                Log.e("sorry","wrong gyroscope data");
                Intent intent = new Intent(context,NotRecognizedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }

        }


    }

    public MessageReceiver(Context context,float[] data){
        this.context=context.getApplicationContext();
        this.data=data;

       dialog = new ProgressDialog(context.getApplicationContext());
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }


    @Override
    protected Void doInBackground(Void... voids) {

        Log.e("hello","messageReceiver");
        Log.e("data from receiver",Float.toString(data[0]));
        Log.e("data from receiver",Float.toString(data[1]));
        Log.e("data from receiver",Float.toString(data[2]));


        Socket socket = null;
        try {
            socket = new Socket("10.7.54.13",839);
            DataInputStream dis= new DataInputStream(socket.getInputStream());

            msg = dis.readUTF();
            if(msg.equals("Not recognized")||msg.equals("face not found")){

            }else {
                splitString=new String[5];
                splitString = msg.split(";");
                msg="welcome "+splitString[0]+" (rozpoznano z prawdopodobieństwem "+splitString[1]+")";
                x=Float.valueOf(splitString[2]);
                y=Float.valueOf(splitString[3]);
                z=Float.valueOf(splitString[4]);
            }

            System.out.println(msg);
            Log.e("najs",msg);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

}
