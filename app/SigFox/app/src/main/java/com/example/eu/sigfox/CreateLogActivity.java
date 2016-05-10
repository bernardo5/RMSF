package com.example.eu.sigfox;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CreateLogActivity extends AppCompatActivity {
    String UsernameApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_log);
        //UsernameApp=getIntent().getExtras().getString("username");
        //Toast.makeText(getBaseContext(), "Hi "+UsernameApp, Toast.LENGTH_LONG).show();
    }

    public void createLog(View view) {
        EditText Name = (EditText) findViewById(R.id.Name);
        EditText Pass = (EditText) findViewById(R.id.password);
        EditText Device_id = (EditText) findViewById(R.id.device_id);
        EditText AppUser = (EditText) findViewById(R.id.editText);
        EditText AppPass = (EditText) findViewById(R.id.editText2);
        UsernameApp=AppUser.getText().toString();
        UsernameApp=UsernameApp.replaceAll(" ","");
        String password=AppPass.getText().toString();
        password=password.replaceAll(" ","");
        try {/************AQUIIIIII*/
            String message=Name.getText().toString().replaceAll(" ","")+"\n"+ Pass.getText().toString().replaceAll(" ","")+"\n"+Device_id.getText().toString().replaceAll(" ","")+"\n";
            FileOutputStream fileOutputStream = openFileOutput(UsernameApp+".txt", MODE_PRIVATE); //no other app can open file
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Log successfully created", Toast.LENGTH_LONG).show();
            new CreateLogDB().execute(UsernameApp, Name.getText().toString().replaceAll(" ",""), Pass.getText().toString().replaceAll(" ",""), Device_id.getText().toString().replaceAll(" ",""), password);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        Intent logged = new Intent(this, MonitorActivity.class);
        logged.putExtra("username", UsernameApp);
        logged.putExtra("messagetime", "0");
        startActivity(logged);
    }

    public class CreateLogDB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = arg0[0].replaceAll("[^a-zA-Z0-9]+", "");
                String link1 ="http://web.tecnico.ulisboa.pt/ist175462/insertUser.php?user="+arg0[0]+"&username="+arg0[1]+"&pass="+arg0[2]+"&device="+arg0[3]+"&filePass="+arg0[4];
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
            Toast.makeText(getBaseContext(), "Log created", Toast.LENGTH_LONG).show();
        }
    }

}
