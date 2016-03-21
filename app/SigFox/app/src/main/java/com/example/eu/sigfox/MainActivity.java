package com.example.eu.sigfox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void nextPage(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        Button button = (Button)view;
        String message = button.getText().toString();
        startActivity(intent);
    }
}
