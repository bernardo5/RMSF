package com.example.eu.sigfox;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class LogsActivity extends AppCompatActivity {
    TextView textView;
    private TextView tvData;
    private String user;
    private String pass;
    private String Device;

    Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> stringArray;

    private String UsernameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        textView=(TextView)findViewById(R.id.login_info);
        textView.setVisibility(View.GONE);

        stringArray = new ArrayList<String>();
       /* stringArray.add("56bdd1da9336b182b106d3b0");
        stringArray.add("othersssDevice");*/

        /*get username from last activity*/
        UsernameApp=getIntent().getExtras().getString("username");
        Toast.makeText(getBaseContext(), "Welcome "+UsernameApp, Toast.LENGTH_LONG).show();
        /**********************************/

        //timer.start();
        login(UsernameApp);

////////////////////////////////////////////////////////////////////////////////////////////
        spinner=(Spinner)findViewById(R.id.spinner);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stringArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parent, View view, int position, long id){
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+" selected", Toast.LENGTH_LONG).show();
                    Device=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }

        });

//////////////////////////////////////////////////////////////////////////////////////////////
        Button btnHit = (Button)findViewById(R.id.sensor_button);

        tvData = (TextView)findViewById(R.id.sensor_text);
        tvData.setVisibility(View.GONE);

        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JSONTask().execute("https://backend.sigfox.com/api/devicetypes/"+Device+"/messages");
            }
        });

    }

    public void checkLogin(View view) {
        File file = new File(getFilesDir(), "logs.txt");
        if(file.exists()){
            //user is logged in
            login(UsernameApp);
        }else{
            //user has to create a log
            Intent create_log = new Intent(this, CreateLogActivity.class);
            startActivity(create_log);
        }
    }

    public void login(String userUsed) {
        int i=0;
        String type=" ";
        String message;
        try{
            FileInputStream fileInputStream = openFileInput(userUsed+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((message = bufferedReader.readLine()) != null) {
                if(i<=1){
                    if(i==0){
                        type="User: ";
                        user=message;
                    }
                    if(i==1){
                        type="Password: ";
                        pass=message;
                    }
                }else{
                   type="Device: ";
                    Device=message;
                    if(!(stringArray.contains(message)))stringArray.add(message);
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
        Toast.makeText(LogsActivity.this,
                "Logout successfully!", Toast.LENGTH_SHORT).show();
        Intent new_log = new Intent(this, CreateLogActivity.class);
        startActivity(new_log);
    }

    public void addAlarm(View view){
        Intent new_alarm = new Intent(this, NewAlarmActivity.class);
        startActivity(new_alarm);
    }

    public void newdevice(View view){
        Intent new_device = new Intent(this, AddDeviceActivity.class);
        startActivity(new_device);
    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {

                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                String encoded = Base64.encodeToString((user + ":" + pass).getBytes("UTF-8"), Base64.NO_WRAP);
                connection.setRequestProperty("Authorization", "Basic " + encoded);
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("data");

                StringBuffer finalBufferedData = new StringBuffer();
                for(int i = 0; i < parentArray.length(); i++){

                    JSONObject finalObject = parentArray.getJSONObject(i);

                    String link_quality = finalObject.getString("linkQuality");
                    double SNR = finalObject.getDouble("snr");
                    finalBufferedData.append("linkQuality - " + link_quality + "\n" + "SNR - " + SNR + "\n");
                }
                return finalBufferedData.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if((connection) != null) {
                    connection.disconnect();
                }
                try {
                    if(reader != null){
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
            tvData.setMovementMethod(new ScrollingMovementMethod());
            tvData.setText(result);
            tvData.setVisibility(View.VISIBLE);

        }
    }






}
