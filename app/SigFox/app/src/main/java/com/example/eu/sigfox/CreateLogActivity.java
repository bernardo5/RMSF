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
        UsernameApp=getIntent().getExtras().getString("username");
        Toast.makeText(getBaseContext(), "Welcome "+UsernameApp, Toast.LENGTH_LONG).show();
    }

    public void createLog(View view) {
        EditText Name = (EditText) findViewById(R.id.Name);
        EditText Pass = (EditText) findViewById(R.id.password);
        EditText Device_id = (EditText) findViewById(R.id.device_id);

        try {/************AQUIIIIII*/
            String message=Name.getText().toString()+"\n"+ Pass.getText().toString()+"\n"+Device_id.getText().toString()+"\n";
            FileOutputStream fileOutputStream = openFileOutput(UsernameApp+".txt", MODE_PRIVATE); //no other app can open file
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Toast.makeText(getApplicationContext(), "Log successfully created", Toast.LENGTH_LONG).show();
            new CreateLogDB().execute(UsernameApp, Name.getText().toString(), Pass.getText().toString(), Device_id.getText().toString());
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        Intent logged = new Intent(this, LogsActivity.class);
        logged.putExtra("username", UsernameApp);
        startActivity(logged);
    }

    public class CreateLogDB extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = (String)arg0[0];
                String link1 ="http://web.tecnico.ulisboa.pt/ist175573/insertUser.php?user="+arg0[0]+"&username="+arg0[1]+"&pass="+arg0[2]+"&device="+arg0[3];
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
            Toast.makeText(getBaseContext(), "Log created in server", Toast.LENGTH_LONG).show();
        }
    }

}
