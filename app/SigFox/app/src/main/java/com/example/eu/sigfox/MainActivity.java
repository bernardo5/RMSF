package com.example.eu.sigfox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                //user has to create a log
                Intent create_log = new Intent(this, CreateLogActivity.class);
                create_log.putExtra("username", UsernameApp);
                startActivity(create_log);
            }

    }



}
