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
    public void checkLogin(View view) {
        File file = new File(getFilesDir(), "logs.txt");
        if(file.exists()){
            //user is logged in
            Intent logged = new Intent(this, LogsActivity.class);
            startActivity(logged);
        }else{
            //user has to create a log
            Intent create_log = new Intent(this, CreateLogActivity.class);
            startActivity(create_log);
        }


    }
}
