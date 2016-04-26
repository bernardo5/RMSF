package com.example.eu.sigfox;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;
    TextView debug;
    TextView devv;

    private String usernameee=new String();
    private String password=new String();
    String dev[];
    int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/

        debug = (TextView)findViewById(R.id.debug);
        debug.setVisibility(View.GONE);

        devv = (TextView)findViewById(R.id.dev);
        devv.setVisibility(View.GONE);
    }

    public void setUsername(String u){
        this.usernameee=u;
    }

    public void setPass(String u){
        this.password=u;
    }

    public String getUsername(){
        return this.usernameee;
    }

    public String getPass(){
        return this.password;
    }

    public void submit(View view) {
            EditText appUsername = (EditText) findViewById(R.id.appUsername);
            String UsernameApp=appUsername.getText().toString();

            File file = new File(getFilesDir(), UsernameApp+".txt");
            if(file.exists()){
                //user is logged in
                Intent logged = new Intent(this, LogsActivity.class);
                logged.putExtra("username", UsernameApp);
                startActivity(logged);
            }else {
                //check database
                new AskServerUserPass().execute(UsernameApp);

                Toast.makeText(getBaseContext(), "Got user and pass from database", Toast.LENGTH_LONG).show();
                /*devv.setText("oi");
                devv.setVisibility(View.VISIBLE);*/
               new AskServerDev().execute(UsernameApp);//executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

                //user has to create a log
               /* Intent create_log = new Intent(this, CreateLogActivity.class);
                create_log.putExtra("username", UsernameApp);
                startActivity(create_log);*/
            }

    }


    public class AskServerUserPass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

                try {
                    String username = (String)arg0[0];
                    String link ="http://web.tecnico.ulisboa.pt/ist175573/userPass.php?username="+username;
                    URL url = new URL(link);

                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet(link);

                    HttpResponse httpResponse = client.execute(request);

                    InputStream inputStream = httpResponse.getEntity().getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }
                    String r=new String();
                    Pattern pattern = Pattern.compile("\\{(.*?)\\}");
                    Matcher matcher = pattern.matcher(stringBuilder.toString());
                    while (matcher.find())
                    {
                        r+=matcher.group(0);
                       // System.out.println(matcher.group(0));
                    }

                    JSONObject jObject = new JSONObject(r);

                    user = jObject.getInt("user");
                    if(user==1){
                        String usernamee = jObject.getString("username");
                        setUsername(usernamee);
                        String password = jObject.getString("password");
                        setPass(password);
                    }


                    return "ok";


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                /*} catch (JSONException e) {
                    e.printStackTrace();*/
                
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
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
            debug.setMovementMethod(new ScrollingMovementMethod());

            result="Username-"+getUsername()+"\n"+"password-"+getPass();

            if(user==0){
                result="You are not logged in yet";
            }

            debug.setText(result);
            debug.setVisibility(View.VISIBLE);
        }
    }

    public class AskServerDev extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

            try {
                String username = (String)arg0[0];
                String link1 ="http://web.tecnico.ulisboa.pt/ist175573/userDev.php?username="+username;
                URL url1 = new URL(link1);

                HttpClient client1 = new DefaultHttpClient();
                HttpGet request1 = new HttpGet(link1);

                HttpResponse httpResponse1 = client1.execute(request1);

                InputStream inputStream1 = httpResponse1.getEntity().getContent();
                InputStreamReader inputStreamReader1 = new InputStreamReader(inputStream1);
                BufferedReader bufferedReader1 = new BufferedReader(inputStreamReader1);

                StringBuilder stringBuilder1 = new StringBuilder();

                String bufferedStrChunk1 = null;

                while((bufferedStrChunk1 = bufferedReader1.readLine()) != null){
                    stringBuilder1.append(bufferedStrChunk1);
                }
                /*String r1=new String();
                Pattern pattern1 = Pattern.compile("\\{(.*?)\\}");
                Matcher matcher1 = pattern1.matcher(stringBuilder1.toString());
                while (matcher1.find())
                {
                    r1+=matcher1.group(0);
                    // System.out.println(matcher.group(0));
                }

                String a1=new String();
                JSONArray jre1;

                jre1 = new JSONArray(r1);

                for (int i = 0; i < jre1.length(); i++)
                {
                   a1 = jre1.getString(i);
                   dev[i]=a1;
                }*/

                return stringBuilder1.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                /*} catch (JSONException e) {
                    e.printStackTrace();*/

            } /*catch (JSONException e) {
                e.printStackTrace();
            }*/ finally {
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

            /*result="Your devices are:\n";

            for(String au:dev){
                result+=au+"\n";
            }*/
            devv.setMovementMethod(new ScrollingMovementMethod());
            devv.setText(result);
            devv.setVisibility(View.VISIBLE);
        }
    }



}
