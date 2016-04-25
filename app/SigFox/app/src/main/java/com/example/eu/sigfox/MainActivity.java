package com.example.eu.sigfox;

import android.os.AsyncTask;
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

    private String usernameee=new String();
    private String password=new String();
    int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debug = (TextView)findViewById(R.id.debug);
        debug.setVisibility(View.GONE);
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
               /* try {
                    URL url = new URL("http://web.tecnico.ulisboa.pt/ist175573/userPass.php");

                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();

                    request.setURI(new URI("http://web.tecnico.ulisboa.pt/ist175573/userPass.php"));


                    HttpResponse response = null;

                    response = client.execute(request);

                    BufferedReader in = new BufferedReader
                            (new InputStreamReader(response.getEntity().getContent()));
                    debug.setText(in.readLine().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }*/

                new AskServer().execute(UsernameApp);


                //user has to create a log
               /* Intent create_log = new Intent(this, CreateLogActivity.class);
                create_log.putExtra("username", UsernameApp);
                startActivity(create_log);*/
            }

    }


    public class AskServer extends AsyncTask<String, String, String> {

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

            result = result.replaceAll("\\r\\n", "");

            String aux[]= result.split(" ");
            result="Username-"+getUsername()+"\n"+"password-"+getPass();

            if(user==0){
                result="You are not logged in yet";
            }

            debug.setText(result);
            debug.setVisibility(View.VISIBLE);
        }
    }



}
