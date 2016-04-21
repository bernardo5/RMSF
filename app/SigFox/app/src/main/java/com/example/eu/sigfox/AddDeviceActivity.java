package com.example.eu.sigfox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddDeviceActivity extends AppCompatActivity {
    String UsernameApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        UsernameApp=getIntent().getExtras().getString("username");
        Toast.makeText(getBaseContext(), "Welcome "+UsernameApp, Toast.LENGTH_LONG).show();
    }

    public void RegisterNewDevice(View view){
        EditText Device_id = (EditText) findViewById(R.id.newDeviceid);

        try {
            String message = Device_id.getText().toString() + "\n";
            FileOutputStream fileOutputStream = openFileOutput(UsernameApp+".txt", MODE_APPEND); //no other app can open file
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            Toast.makeText(AddDeviceActivity.this,
                    "Successfully added a new device!", Toast.LENGTH_SHORT).show();
            finish();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
