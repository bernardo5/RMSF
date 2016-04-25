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

import org.apache.http.HttpEntity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        debug = (TextView)findViewById(R.id.debug);
        debug.setVisibility(View.GONE);
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

                    HttpClient client = new DefaultHttpClient();

                    HttpGet httpGet = new HttpGet(link);
                    HttpResponse httpResponse = client.execute(httpGet);
                    HttpEntity httpEntity = httpResponse.getEntity();


                    //HttpResponse httpResponse = client.execute(request);

                    InputStream inputStream = httpResponse.getEntity().getContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder builder = new StringBuilder();

                    String bufferedStrChunk = null;

                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        builder.append(bufferedStrChunk);
                    }


                    /*String finalJson = builder.toString();

                    JSONObject parentObject = new JSONObject(finalJson);
                    JSONArray parentArray = parentObject.getJSONArray("data");

                    StringBuffer finalBufferedData = new StringBuffer();
                    for(int i = 0; i < parentArray.length(); i++){

                        JSONObject finalObject = parentArray.getJSONObject(i);

                        String user = finalObject.getString("user");
                        String usernamee = finalObject.getString("username");
                        String password = finalObject.getString("password");
                        finalBufferedData.append("User - " + user + "\n" + "username - " + usernamee+"\n" + "password-"+password+"\n");
                    }*/

                    String content=builder.toString();

                    String regex = "\\s*\\bhtml\\b\\s*";
                    content = content.replaceAll(regex, "");

                    regex = "\\s*\\bbody\\b\\s*";
                    content = content.replaceAll(regex, "");

                    regex = "[<>/]";
                    content = content.replaceAll(regex, "");

                    /*String finalJson = content;

                    JSONObject parentObject = new JSONObject(finalJson);
                    JSONArray parentArray = parentObject.getJSONArray("data");

                    StringBuffer finalBufferedData = new StringBuffer();
                    for(int i = 0; i < parentArray.length(); i++) {

                        JSONObject finalObject = parentArray.getJSONObject(i);

                        String user = finalObject.getString("user");
                        String usernamee = finalObject.getString("username");
                        String password = finalObject.getString("password");
                        finalBufferedData.append("User - " + user + "\n" + "username - " + usernamee + "\n" + "password-" + password + "\n");
                    }*/

                    

                    return content;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } /*catch (JSONException e) {
                    e.printStackTrace();


                } */finally {
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
            debug.setText(result);
            debug.setVisibility(View.VISIBLE);

        }
    }



}
