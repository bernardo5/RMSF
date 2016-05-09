package com.example.eu.sigfox;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorActivity extends AppCompatActivity {
    private TextView tvData;
    private String user;
    private String pass;
    private String Device;
    Timer t ;
    MyTimerTask timerTask;
    final Handler handler = new Handler();
    Spinner spinner;
    ArrayAdapter<String> adapter;
    ArrayList<String> stringArray;
boolean all=false;
    String messageTime;

    private String UsernameApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);


       messageTime=getIntent().getExtras().getString("messagetime");

        stringArray = new ArrayList<String>();
        UsernameApp=getIntent().getExtras().getString("username");
        Toast.makeText(getBaseContext(), "Welcome "+UsernameApp, Toast.LENGTH_LONG).show();
        /**********************************/
        //timer.start();
        File file = new File(getFilesDir(), UsernameApp + ".txt");

        if (file.exists()) {
            //user is logged in
            login(UsernameApp);
        }

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
        //Toast.makeText(getBaseContext(), "Initial message timestamp: "+messageTime, Toast.LENGTH_LONG).show();
        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getBaseContext(), "Last message timestamp: "+messageTime, Toast.LENGTH_LONG).show();
                if(all==false)
                    new JSONTask().execute("https://backend.sigfox.com/api/devicetypes/" + Device + "/messages?since="+messageTime);
                else  new JSONTask().execute("https://backend.sigfox.com/api/devicetypes/" + Device + "/messages");
            }
        });



        CheckBox repeatChkBx = (CheckBox) findViewById( R.id.checkBox2);
        repeatChkBx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(t != null){
                    t.cancel();
                }
                if (isChecked) {
                    t = new Timer();
                    timerTask = new MyTimerTask();
                    t.schedule(timerTask, 1000, 600000);
                }else{
                    t.cancel();
                    t=null;
                }

            }
        });

        CheckBox allmessages = (CheckBox) findViewById( R.id.checkBox);
        allmessages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                all = isChecked;

            }
        });



    }

    public void deleteInfo(View view) {
        tvData.setText(" ");
    }

    public void login(String userUsed) {
        int i=0;
        String type=" ";
        String message;
        new  getAlarms().execute(UsernameApp);
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

            fileInputStream.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void logout(View view){
        Toast.makeText(MonitorActivity.this,
                "Logout successfull!", Toast.LENGTH_SHORT).show();
        Intent new_log = new Intent(this, MainActivity.class);
        startActivity(new_log);
    }

    public void addAlarm(View view){
        Intent new_alarm = new Intent(this, NewAlarmActivity.class);
        new_alarm.putExtra("username", UsernameApp);
        new_alarm.putExtra("messagetime", messageTime);
        startActivity(new_alarm);
        //finish();
    }

    public void newdevice(View view){
        Intent new_device = new Intent(this, AddDeviceActivity.class);
        new_device.putExtra("username", UsernameApp);
        new_device.putExtra("messagetime", messageTime);
        startActivity(new_device);
    }
    @TargetApi(16)
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
                int timestamp;
                java.util.Date time=null;
                int timeRecent=0;
                for(int i = 0; i < parentArray.length(); i++) {

                    JSONObject finalObject = parentArray.getJSONObject(i);

                    String link_quality = finalObject.getString("linkQuality");
                    double SNR = finalObject.getDouble("snr");
                    timestamp = finalObject.getInt("time");
                    if (i == 0) timeRecent = timestamp;
                    String data = finalObject.getString("data");
                    if (data.length() >= 8) {
                        String rev_data = new StringBuffer(data).reverse().toString();
                        String pair1 = new StringBuffer(rev_data.substring(0, 2)).reverse().toString();
                        String pair2 = new StringBuffer(rev_data.substring(2, 4)).reverse().toString();
                        String pair3 = new StringBuffer(rev_data.substring(4, 6)).reverse().toString();
                        String pair4 = new StringBuffer(rev_data.substring(6, 8)).reverse().toString();
                        String final_data = pair1 + pair2 + pair3 + pair4;
                        Long in = Long.parseLong(final_data, 16);
                        Float f = Float.intBitsToFloat(in.intValue());
                        if(f>= 1E-3 && f<=50 ) {
                            String print_data = Float.toString(f);
                            time = new java.util.Date((long) timestamp * 1000);
                            finalBufferedData.append("message at: " + time + "\n" + "linkQuality: " + link_quality + "\n" + "SNR: " + SNR + "\n" + "Message: " + print_data + "ºC" + "\n" + "delimiter");

                        }else{
                            String print_data = "Temperature out of sensor range";
                            time = new java.util.Date((long) timestamp * 1000);
                            finalBufferedData.append("message at: " + time + "\n" + "linkQuality: " + link_quality + "\n" + "SNR: " + SNR + "\n" + "Message: " + print_data + "\n" + "delimiter");
                        }
                    }
                }
                return finalBufferedData.toString() + " # "+timeRecent;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            /*} catch (DecoderException e) {
                e.printStackTrace();*/
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if ((result!=null)&&(result.indexOf("message")!=-1)){

                String[] parts = result.split(" # ");
                String part1 = parts[0]; // 004
                String part2 = parts[1];

                String[] reads=part1.split("delimiter");
                String toPrint=new String();
                //prepare text to print
                for(String s:reads){
                    toPrint+=s+"\n\n";
                }
                ////////////////////////
                //print text
                tvData.setMovementMethod(new ScrollingMovementMethod());
                if(!all) tvData.append(toPrint);
                else tvData.setText(toPrint);
                tvData.setVisibility(View.VISIBLE);
                ///////////////////////

                //gets alarms
                File file = new File(getFilesDir(), UsernameApp+"-alarms.txt");
                if(file.exists()&&(!all)){
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = openFileInput(UsernameApp+"-alarms.txt");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();

                    String line;
                    ArrayList<Float> alarms=new ArrayList<Float>();

                    try {
                        while ((line = bufferedReader.readLine()) != null) {
                            line = line.replace("\n", "").replace("\r", "");
                            line=line.replaceAll("ºC", "");
                            Float number=Float.parseFloat(line);
                            alarms.add(number);
                        }
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Collections.sort(alarms);
                    int x=0;
                    //condicao alarme

                   // Toast.makeText(getBaseContext(), "if", Toast.LENGTH_LONG).show();
                    for(String s1:reads) {
                        // "SNR - " + SNR + "\n"+"Message-"+
                        if (s1.indexOf("ºC") > 0){
                            String aux = s1.substring(s1.indexOf("Message: ") + 8, s1.indexOf("ºC"));
                            aux = aux.replace("\n", "").replace("\r", "");
                            for (float f : alarms) {
                                x++;
                                if (Float.parseFloat(aux) >= f) {
                                    NotificationCompat.Builder mBuilder =
                                            (NotificationCompat.Builder) new NotificationCompat.Builder(getBaseContext())
                                                    .setSmallIcon(R.drawable.ic_alert)
                                                    .setContentTitle("Threshold overflow!")
                                                    .setContentText("Threshold violated: " + f + " with temperature " + aux);

                                    Intent resultIntent = new Intent(getBaseContext(), MonitorActivity.class);
                                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
                                    // Adds the back stack for the Intent (but not the Intent itself)
                                    stackBuilder.addParentStack(MonitorActivity.class);
                                    // Adds the Intent that starts the Activity to the top of the stack
                                    stackBuilder.addNextIntent(resultIntent);
                                    mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                    //Id allows you to update the notification later on.
                                    mNotificationManager.notify(100 + x, mBuilder.build());
                                    try {
                                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                        r.play();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }
                }

                int t=Integer.parseInt(part2);
                t+=1;
                //update most recent time
                //FileOutputStream overWrite = null;
                messageTime = Integer.toString(t);
               //update database with t value!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                new UpdateLastRead().execute(UsernameApp, messageTime);

              //  Toast.makeText(getBaseContext(), messageTime, Toast.LENGTH_LONG).show();
            }else{
                tvData.append("Current device does not have new messages...\n");
                tvData.setVisibility(View.VISIBLE);
            }
        }
    }


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
          runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    Toast.makeText(MonitorActivity.this,
                            "Update!", Toast.LENGTH_SHORT).show();
                    if(all==false)
                        new JSONTask().execute("https://backend.sigfox.com/api/devicetypes/" + Device + "/messages?since="+messageTime);
                    else  new JSONTask().execute("https://backend.sigfox.com/api/devicetypes/" + Device + "/messages");

                }});
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
                        disp += s + "\n";
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
           /* Toast.makeText(getBaseContext(), "processing", Toast.LENGTH_LONG).show();


            Toast.makeText(getApplicationContext(), "Current alarms: "+result, Toast.LENGTH_LONG).show();*/



        }
    }


    public class UpdateLastRead extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = arg0[0];
                String link1 ="http://web.tecnico.ulisboa.pt/ist175462/insertLastTime.php?user="+arg0[0]+"&time="+arg0[1];
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
           // Toast.makeText(getBaseContext(), "Time updated in server", Toast.LENGTH_LONG).show();
        }
    }
}
