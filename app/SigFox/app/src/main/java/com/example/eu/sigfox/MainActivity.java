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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    TextView log;
    String UsernameApp;
    Button cont;
    Button sub;
    Button sign;
    private String messageTime;

    private String usernameee=new String();
    private String password=new String();
    String dev[];
    int user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = (TextView)findViewById(R.id.log);
        log.setVisibility(View.GONE);

        cont = (Button) findViewById(R.id.continuee);
        sign=(Button) findViewById(R.id.signup);
        cont.setVisibility(View.GONE);
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

    public void setMessageTime(String u){
        this.messageTime=u;
    }

    public String getMessageTime(){
        return this.messageTime;
    }

    public void submit(View view) {
            sub = (Button) findViewById(R.id.button);

            EditText appUsername = (EditText) findViewById(R.id.appUsername);
            EditText userpass = (EditText) findViewById(R.id.userpassapp);
            UsernameApp=appUsername.getText().toString();
            String pass=userpass.getText().toString();

            File file = new File(getFilesDir(), UsernameApp+".txt");
            if(file.exists()){

                File dir = getFilesDir();
                File f = new File(dir, UsernameApp+".txt");
                boolean deleted = f.delete();


            }
                //check database
                new AskServerUserPass().execute(UsernameApp, pass);

              //  Toast.makeText(getBaseContext(), "Database read successfully", Toast.LENGTH_LONG).show();


    }

    public void nextActivity(View view){
        String loggedd = log.getText().toString();
        loggedd=loggedd.replaceAll("\n", "");

        //user is logged in
         Intent logged = new Intent(this, LogsActivity.class);
         logged.putExtra("username", UsernameApp);
         logged.putExtra("messagetime", getMessageTime());
         startActivity(logged);

    }

    public void create_log(View view){
        //user has to create a log
        Intent create_log = new Intent(this, CreateLogActivity.class);
        //create_log.putExtra("username", UsernameApp);
        startActivity(create_log);
    }


    public class AskServerUserPass extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //TextView debug;

                try {
                    String username = arg0[0];
                    String p=arg0[1];
                    String link ="http://web.tecnico.ulisboa.pt/ist175462/userPass.php?username="+username+"&password="+p;
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
                    }

                    JSONObject jObject = new JSONObject(r);

                    user = jObject.getInt("user");
                    if(user==1){
                        String usernamee = jObject.getString("username");
                        setUsername(usernamee);
                        String password = jObject.getString("password");
                        setPass(password);
                        String messageTimee = jObject.getString("lastMessageTime");
                        setMessageTime(messageTimee);

                        try {
                            String message=usernamee+"\n"+ password+"\n";
                            FileOutputStream fileOutputStream = openFileOutput(arg0[0]+".txt", MODE_PRIVATE); //no other app can open file
                            fileOutputStream.write(message.getBytes());
                            fileOutputStream.close();
                        }catch(FileNotFoundException e){
                            e.printStackTrace();
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                    }


                    return "ok";


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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
            log.setMovementMethod(new ScrollingMovementMethod());

            result="You are registered as:\n"+getUsername()+"\n"+"*****************************************\n";

            if(user==0){
                result="You are not logged in yet";
                //sign.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "You are not logged in yet. Please try again or sign up!", Toast.LENGTH_LONG).show();
            }else{
                new AskServerDev().execute(UsernameApp);
                sub.setVisibility(View.GONE);
                sign.setVisibility(View.GONE);
            }

            log.setText(result);
            log.setVisibility(View.VISIBLE);
        }
    }

    public class AskServerDev extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... arg0) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                String username = arg0[0];
                String link1 ="http://web.tecnico.ulisboa.pt/ist175462/userDev.php?username="+username;
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


                String r = stringBuilder1.toString().substring(stringBuilder1.toString().indexOf("["), stringBuilder1.toString().indexOf("]")+1);

                String teste=new String();
                int i=0, j=0;
               while(r.indexOf("\"", j+1)!=(-1)){
                    i=r.indexOf("\"", j+1);
                   j=r.indexOf("\"", i+1);
                   teste+=r.substring(i+1,j)+" ";
               }

                return teste;


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
           // Toast.makeText(getBaseContext(), "processing", Toast.LENGTH_LONG).show();

            String disp="Your devices are:\n";

            dev=result.split("\\s+");

            for(String s:dev){
                disp+=s+"\n";
            }

            log.setMovementMethod(new ScrollingMovementMethod());
            log.append(disp);

            disp = disp.replace("Your devices are:\n","");

            try {
                FileOutputStream fileOutputStream = openFileOutput(UsernameApp+".txt", MODE_APPEND); //no other app can open file
                fileOutputStream.write(disp.getBytes());
                fileOutputStream.close();
               // Toast.makeText(getApplicationContext(), "File written!", Toast.LENGTH_LONG).show();
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            cont.setVisibility(View.VISIBLE);

        }
    }



}
