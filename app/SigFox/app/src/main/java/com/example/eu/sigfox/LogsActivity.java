package com.example.eu.sigfox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogsActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        textView=(TextView)findViewById(R.id.login_info);
        textView.setVisibility(View.GONE);
    }

    public void checkLogin(View view) {
        File file = new File(getFilesDir(), "logs.txt");
        if(file.exists()){
            //user is logged in
            login();
        }else{
            //user has to create a log
            Intent create_log = new Intent(this, CreateLogActivity.class);
            startActivity(create_log);
        }
    }

    public void login() {
        int i=0;
        String type=" ";
        String message;
        try{
            FileInputStream fileInputStream = openFileInput("logs.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((message = bufferedReader.readLine()) != null) {
                if(i<=1){
                    if(i==0) type="User: ";
                    if(i==1) type="Password: ";
                }else{
                   type="Device: ";
                }
                stringBuffer.append(type + message + "\n");
                i++;
            }
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setText(stringBuffer.toString());
            textView.setVisibility(View.VISIBLE);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void logout(View view){
        File file=new File(getFilesDir(), "logs.txt");
        boolean deleted=file.delete();
        if(deleted==true){ /*deleted file*/
            Toast.makeText(LogsActivity.this,
                    "Logout successfully!", Toast.LENGTH_SHORT).show();
            Intent new_log = new Intent(this, CreateLogActivity.class);
            startActivity(new_log);
        }else{
            Toast.makeText(LogsActivity.this,
                    "Couldn't logout!", Toast.LENGTH_SHORT).show();
        }

    }

    public void addAlarm(View view){
        Intent new_alarm = new Intent(this, NewAlarmActivity.class);
        startActivity(new_alarm);
    }

    public void newdevice(View view){
        Intent new_device = new Intent(this, AddDeviceActivity.class);
        startActivity(new_device);
    }

}
