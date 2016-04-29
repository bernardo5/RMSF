package com.example.eu.sigfox;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewAlarmActivity extends AppCompatActivity {
    String UsernameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        UsernameApp=getIntent().getExtras().getString("username");
        Toast.makeText(getBaseContext(), "Welcome "+UsernameApp, Toast.LENGTH_LONG).show();
    }

    public void RegisterNewAlarm(EditText alarm_threshold){
        try {
            String message = alarm_threshold.getText().toString() + "\n";
            FileOutputStream fileOutputStream = openFileOutput(UsernameApp+"-alarms.txt", MODE_APPEND); //no other app can open file
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
       /* File file = new File(getFilesDir(), UsernameApp+"-alarms.txt");
        if(file.exists()){
            RegisterNewAlarm(alarm_threshold);
        }else{
            try {
                String message=alarm_threshold.getText().toString()+"\n";
                FileOutputStream fileOutputStream = openFileOutput(UsernameApp+"-alarms.txt", MODE_PRIVATE); //no other app can open file
                fileOutputStream.write(message.getBytes());
                fileOutputStream.close();
                Toast.makeText(getApplicationContext(), "Alarm successfully created!", Toast.LENGTH_LONG).show();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
        }*/
        new CreateLogDB().execute(UsernameApp,alarm_threshold.getText().toString());
        finish();
    }

    public class CreateLogDB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = (String)arg0[0];
                String link1 ="http://web.tecnico.ulisboa.pt/ist175573/insertAlarm.php?user="+arg0[0]+"&Alarm="+arg0[1];
                URL url1 = new URL(link1);

                HttpClient client1 = new DefaultHttpClient();
                HttpGet request1 = new HttpGet(link1);

                HttpResponse httpResponse1 = client1.execute(request1);



                return "ok";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if ((connection) != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;

        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Toast.makeText(getBaseContext(), "Alarm registed in server", Toast.LENGTH_LONG).show();
        }
    }
}
