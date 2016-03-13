package com.example.bernardo.socket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.*;

public class MainActivity extends AppCompatActivity {

    EditText textoSaida;
    TextView textoEntrada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoSaida=(EditText) findViewById(R.id.textosaida);
        textoEntrada=(TextView) findViewById(R.id.textoentrada);

        Button botaoenviar= (Button) findViewById(R.id.enviar);
        botaoenviar.setOnClickListener(buttonSendOnClickListener);
    }

    View.OnClickListener buttonSendOnClickListener = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0){
            Socket socket=null;
            DataOutputStream dataOutputStream=null;
            DataInputStream dataInputStream=null;

            try{
                socket=new Socket("192.168.1.77", 8080);

                dataOutputStream=new DataOutputStream(socket.getOutputStream());
                dataInputStream= new DataInputStream(socket.getInputStream());

                dataOutputStream.writeUTF(textoSaida.getText().toString());

                String stringLida=dataInputStream.readLine();

                textoEntrada.setText(stringLida);

            }catch(Exception e){
                e.printStackTrace();
            }finally {
                try{
                    if(socket!=null){
                        socket.close();
                    }
                    if(dataOutputStream!=null){
                        dataOutputStream.close();
                    }
                    if(dataInputStream!=null){
                        dataInputStream.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        }
    };
}
