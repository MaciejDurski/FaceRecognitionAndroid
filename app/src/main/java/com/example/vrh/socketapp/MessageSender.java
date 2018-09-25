package com.example.vrh.socketapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class MessageSender extends AsyncTask<Bitmap,Void,Void> {
    Socket s;
    DataOutput dataOutput;
    PrintWriter printWriter;
    String encodedImage;
    Bitmap image;
    Context context;

    public MessageSender(Context context){
        this.context=context.getApplicationContext();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MessageReceiver messageReceiver = new MessageReceiver(context);
        messageReceiver.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Bitmap... voids) {
        image= voids[0];
        try  {



         //zakodowanie zdjecia

            Log.e("message sender", "message");


           ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,50, baos);
            byte [] b=baos.toByteArray();
            baos.close();
            encodedImage=Base64.encodeToString(b, Base64.DEFAULT);









            Log.e("zakodowaneZdjecie",String.valueOf(encodedImage.length()));
            Log.e("",encodedImage);


            Socket socket = s = new Socket("10.7.54.13", 839);
            printWriter = new PrintWriter(socket.getOutputStream());

            printWriter.write(encodedImage);
            printWriter.flush();
            printWriter.close();
            socket.close();










        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
