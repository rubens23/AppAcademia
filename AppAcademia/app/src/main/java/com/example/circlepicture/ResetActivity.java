package com.example.circlepicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ResetActivity extends AppCompatActivity {

    TextView username;
    EditText pass, repass;
    Button confirm;
    MyDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        username = (TextView) findViewById(R.id.username_reset_text);
        pass = (EditText) findViewById(R.id.password_reset);
        repass = (EditText) findViewById(R.id.repassword_reset);
        confirm = (Button) findViewById(R.id.btnconfirm);
        db = new MyDatabaseHelper(ResetActivity.this);

        Intent intent = getIntent();
        username.setText(intent.getStringExtra("username"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString().trim();
                String password = pass.getText().toString().trim();
                String repassword = repass.getText().toString().trim();
                if(password.equals(repassword)) {

                    Boolean checkpasswordupdate = db.updatepassword(user, password);
                    if (checkpasswordupdate) {
                        Intent intent = new Intent(getApplicationContext(), MainActivityKT.class);
                        startActivity(intent);
                        Toast.makeText(ResetActivity.this, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ResetActivity.this, "Falha ao alterar senha", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ResetActivity.this, "As senhas digitadas não são iguais", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}