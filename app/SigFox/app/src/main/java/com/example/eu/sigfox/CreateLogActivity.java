package com.example.eu.sigfox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);
    }

    public void createLog(View view) {
        EditText Name = (EditText) findViewById(R.id.Name);
        EditText Pass = (EditText) findViewById(R.id.password);
        EditText Device_id = (EditText) findViewById(R.id.device_id);

        try {/************AQUIIIIII*/
            String message=Name.getText().toString()+"\n"+ Pass.getText().toString()+"\n"+Device_id.getText().toString()+"\n";
            FileOutputStream fileOutputStream = openFileOutput(/*Name.getText()+*/"logs.txt", MODE_PRIVATE); //no other app can open file
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Log successfully created", Toast.LENGTH_LONG).show();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        finish();
    }
}
