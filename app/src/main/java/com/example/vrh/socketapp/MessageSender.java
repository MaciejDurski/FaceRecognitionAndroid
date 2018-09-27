package com.example.vrh.socketapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

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
    float[] data;

    private ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Sending photo...");
        dialog.show();
    }

    public MessageSender(Context context, float[] data){
        this.context=context.getApplicationContext();
        this.data=data;

        dialog = new ProgressDialog(context.getApplicationContext());
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
}

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        MessageReceiver messageReceiver = new MessageReceiver(context,data);
        messageReceiver.execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Bitmap... voids) {
        image= voids[0];
        try  {



         //zakodowanie zdjecia

            Log.e("message sender", "message");
            Log.e("data from sender",Float.toString(data[0]));


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
