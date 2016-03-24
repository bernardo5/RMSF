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

public class NewAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
    }

    public void RegisterNewAlarm(EditText alarm_threshold){
        try {
            String message = alarm_threshold.getText().toString() + "\n";
            FileOutputStream fileOutputStream = openFileOutput("alarms.txt", MODE_APPEND); //no other app can open file
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Toast.makeText(NewAlarmActivity.this,
                    "Successfully added a new alarm!", Toast.LENGTH_SHORT).show();
            finish();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void createAlarm(View view){
        EditText alarm_threshold = (EditText) findViewById(R.id.temperatureThreshold);
        File file = new File(getFilesDir(), "logs.txt");
        if(file.exists()){
            RegisterNewAlarm(alarm_threshold);
        }else{
            try {
                String message=alarm_threshold.getText().toString()+"\n";
                FileOutputStream fileOutputStream = openFileOutput("alarms.txt", MODE_PRIVATE); //no other app can open file
                fileOutputStream.write(message.getBytes());
                fileOutputStream.close();
                Toast.makeText(getApplicationContext(), "Alarm successfully created!", Toast.LENGTH_LONG).show();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        finish();
    }
}
