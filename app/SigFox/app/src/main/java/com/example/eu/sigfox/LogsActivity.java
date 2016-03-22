package com.example.eu.sigfox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogsActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        textView=(TextView)findViewById(R.id.login_info);
        textView.setVisibility(View.GONE);
    }

    public void login(View view) throws IOException {
        String message;
        FileInputStream fileInputStream=openFileInput("logs.txt");
        InputStreamReader inputStreamReader=new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
        StringBuffer stringBuffer= new StringBuffer();
        while((message=bufferedReader.readLine())!=null){
            stringBuffer.append(message+"\n");
        }
        textView.setText(stringBuffer.toString());
        textView.setVisibility(View.VISIBLE);
    }

}
