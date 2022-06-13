package com.example.circlepicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class paginaRegistro extends AppCompatActivity {

    EditText ETemail, ETsenha, ETConfirmaSenha, ETnome, ETsobrenome, ETcidade, ETestado, ETpais;
    Button btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_registro);


        ETemail = findViewById(R.id.ETemail);
        ETsenha = findViewById(R.id.ETsenha);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyDatabaseHelper db = new MyDatabaseHelper(paginaRegistro.this);
                Boolean result = db.checkEmail(ETemail.getText().toString());//sera que eu tenho que utilizar o trim aqui tbm
                if (ETnome.getText().toString().equals("") || ETsobrenome.getText().toString().equals("") || ETcidade.getText().toString().equals("") ||
                        ETestado.getText().toString().equals("") || ETpais.getText().toString().equals("") ||
                        ETemail.getText().toString().equals("") || ETsenha.getText().toString().equals("") ||
                        ETConfirmaSenha.getText().toString().equals("")){
                    Toast.makeText(paginaRegistro.this, "Preencha todos os campos!", Toast.LENGTH_LONG).show();
                }else{
                    if(ETsenha.getText().toString().trim().equals(ETConfirmaSenha.getText().toString().trim())){
                        if(!result){
                            db.addAccount(ETnome.getText().toString().trim(),
                                    ETsobrenome.getText().toString().trim(),
                                    ETcidade.getText().toString().trim(),
                                    ETestado.getText().toString().trim(),
                                    ETpais.getText().toString().trim(),
                                    ETemail.getText().toString().trim(),
                                    ETsenha.getText().toString().trim());

                        }else{
                            Toast.makeText(paginaRegistro.this, "O email informado já está cadastrado. Faça login", Toast.LENGTH_LONG).show();
                    }
                }else{
                        Toast.makeText(paginaRegistro.this, "As duas senhas digitadas não são iguais!", Toast.LENGTH_LONG).show();
                    }

                }



            }


        });
    }
}

//20:21

/*
else{
                    if(senha.equals(ConfirmacaoSenha)){
                        Boolean checkuser = DB.checkemail(email);
                        if(checkuser==false){
                            Boolean insert = DB.addUser(nome, sobrenome, cidade, estado,
                                    pais, email, senha);
                            if(insert==true){
                                Toast.makeText(paginaRegistro.this, "Registered succesfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(paginaRegistro.this, "Registration failed", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(paginaRegistro.this, "User already exists", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(paginaRegistro.this, "Passwords not matching", Toast.LENGTH_LONG).show();
                    }
                }
 */