package com.example.vrh.socketapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {


    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    ImageView imageToUpload;

    Button btn;
    Button receive;

    Bitmap image;
    Uri capturedImageUri;

    ///////////////////////////////

    private SensorManager sensorManager;
    private Sensor gryoscopeSensor;
    private SensorEventListener sensorEventListener;
    private float[] gyroscopeData;
    private float[] data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageToUpload = (ImageView) findViewById(R.id.imageToUpload);
        imageToUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
////////////////////////////////////////////////////////
       gyroscopeData = new float[3];


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gryoscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        if(gryoscopeSensor==null){
            Toast.makeText(this,"nie ma zyroskopu",Toast.LENGTH_LONG).show();
            finish();
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                gyroscopeData=sensorEvent.values.clone();


                System.out.println("x: "+sensorEvent.values[0]);
                System.out.println("y: "+sensorEvent.values[1]);
                System.out.println("z: "+sensorEvent.values[2]);




            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };



        ///////////////////////////////////////////////////////////

        btn = (Button) findViewById(R.id.btn_send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                send();
            }
        });


///////////////////////////////////////////




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {


            capturedImageUri = data.getData();
            Bitmap bitmap = null;
            try {
                InputStream is = getContentResolver().openInputStream(capturedImageUri);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(270);

            Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);



            imageToUpload.setImageBitmap(rotated);

        }
    }



    public void send()  {


        image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();





      data= new float[3];
        data=gyroscopeData.clone();
        Log.e("okko",Float.toString(data[0]));
       onPause();

        MessageSender messageSender = new MessageSender(MainActivity.this,data);
        messageSender.execute(image);
    }



    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener,gryoscopeSensor,SensorManager.SENSOR_DELAY_FASTEST);

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

}


