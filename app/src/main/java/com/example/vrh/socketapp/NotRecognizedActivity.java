package com.example.vrh.socketapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NotRecognizedActivity extends AppCompatActivity {
    Button login;
    EditText password;
    private static final String PASSWORD="1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_recognized);
        login=(Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password=(EditText) findViewById(R.id.pass_ET);
                int i=0;
                while (i<=3) {
                    if (PASSWORD.equals(password.getText().toString())) {
                        Intent intent = new Intent(NotRecognizedActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        break;
                    }else{
                        Toast.makeText(NotRecognizedActivity.this,"Wrong password, try again",Toast.LENGTH_SHORT).show();
                        i++;
                    }
                }

            }
        });

    }
}
