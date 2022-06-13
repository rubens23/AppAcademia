package com.example.circlepicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class resetarSenha extends AppCompatActivity {

    EditText username;
    Button reset;
    MyDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetar_senha);

        username = (EditText) findViewById(R.id.username_reset);
        reset = (Button) findViewById(R.id.btnReset);
        db = new MyDatabaseHelper(resetarSenha.this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString().trim();

                Boolean checkuser = db.checkEmail(user);
                if(checkuser){
                    Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                }else{
                    Toast.makeText(resetarSenha.this, "email não encontrado.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}

//7:27