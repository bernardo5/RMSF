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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddDeviceActivity extends AppCompatActivity {
    String UsernameApp;
    String messageTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        UsernameApp=getIntent().getExtras().getString("username");
        Toast.makeText(getBaseContext(), "Hi "+UsernameApp, Toast.LENGTH_LONG).show();
        messageTime=getIntent().getExtras().getString("messagetime");
    }

    public void RegisterNewDevice(View view){
        EditText Device_id = (EditText) findViewById(R.id.newDeviceid);

        try {
            String message = Device_id.getText().toString().replaceAll(" ", "") + "\n";
            FileOutputStream fileOutputStream = openFileOutput(UsernameApp+".txt", MODE_APPEND); //no other app can open file
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Toast.makeText(AddDeviceActivity.this,
                    "Successfully added a new device!", Toast.LENGTH_SHORT).show();
            new AddNewDevice().execute(UsernameApp, Device_id.getText().toString().replaceAll(" ", ""));
            Intent logged = new Intent(this, MonitorActivity.class);
            logged.putExtra("username", UsernameApp);
            logged.putExtra("messagetime", messageTime);
            startActivity(logged);
            finish();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public class AddNewDevice extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = arg0[0];
                String link1 ="http://web.tecnico.ulisboa.pt/ist175462/insertDevice.php?user="+arg0[0]+"&device="+arg0[1];
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
            Toast.makeText(getBaseContext(), "Successfully added a new device!", Toast.LENGTH_LONG).show();
        }
    }
}
