package com.example.eu.sigfox;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NewAlarmActivity extends AppCompatActivity {
    String UsernameApp;
    String messageTime;
    TextView alarms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        UsernameApp=getIntent().getExtras().getString("username");
        Toast.makeText(getBaseContext(), "Hi "+UsernameApp, Toast.LENGTH_LONG).show();
        messageTime=getIntent().getExtras().getString("messagetime");
        alarms = (TextView)findViewById(R.id.textView6);
        alarms.setMovementMethod(new ScrollingMovementMethod());
        new  getAlarms().execute(UsernameApp);
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
        new CreateLogDB().execute(UsernameApp,alarm_threshold.getText().toString());
        Intent logged = new Intent(this, LogsActivity.class);
        logged.putExtra("username", UsernameApp);
        logged.putExtra("messagetime", messageTime);
        startActivity(logged);
        finish();
    }

    public class CreateLogDB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = arg0[0];
                String link1 ="http://web.tecnico.ulisboa.pt/ist175462/insertAlarm.php?user="+arg0[0]+"&Alarm="+arg0[1];
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
            Toast.makeText(getBaseContext(), "Alarm added", Toast.LENGTH_LONG).show();
        }
    }

    public class getAlarms extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = arg0[0];
                String link1 = "http://web.tecnico.ulisboa.pt/ist175462/userAlarms.php?username=" + username;
                URL url1 = new URL(link1);

                HttpClient client1 = new DefaultHttpClient();
                HttpGet request1 = new HttpGet(link1);

                HttpResponse httpResponse1 = client1.execute(request1);

                File file = new File(getFilesDir(), UsernameApp+".txt");
                if(file.exists()){
                    File dir = getFilesDir();
                    File f = new File(dir, arg0[0] + "-alarms.txt");
                    boolean deleted = f.delete();
                }


                InputStream inputStream1 = httpResponse1.getEntity().getContent();
                InputStreamReader inputStreamReader1 = new InputStreamReader(inputStream1);
                BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);

                StringBuilder stringBuilder1 = new StringBuilder();

                String bufferedStrChunk1 = null;

                while ((bufferedStrChunk1 = bufferedReader1.readLine()) != null) {
                    stringBuilder1.append(bufferedStrChunk1);
                }
                String disp = new String();
                String teste = new String();
                String r=new String();

                if (!stringBuilder1.toString().toLowerCase().contains("null")){

                    r = stringBuilder1.toString().substring(stringBuilder1.toString().indexOf("["), stringBuilder1.toString().indexOf("]") + 1);



                    int i = 0, j = 0;
                    while (r.indexOf("\"", j + 1) != (-1)) {
                        i = r.indexOf("\"", j + 1);
                        j = r.indexOf("\"", i + 1);
                        teste += r.substring(i + 1, j) + " ";
                    }




                    String alarms[] = teste.split("\\s+");

                    for (String s : alarms) {
                        disp += s+ "ºC" + "\n";
                    }

                    try {
                        FileOutputStream fileOutputStream = openFileOutput(arg0[0] + "-alarms.txt", MODE_PRIVATE); //no other app can open file
                        fileOutputStream.write(disp.getBytes());
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return disp;


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

            alarms.setText(result);

        }
    }
}
