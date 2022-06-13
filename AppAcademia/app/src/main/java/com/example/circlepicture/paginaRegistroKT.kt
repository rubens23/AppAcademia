package com.example.circlepicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_pagina_registro.*


class paginaRegistroKT : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private lateinit var ETEmail : EditText
    private lateinit var ETSenha : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_registro)

        auth = FirebaseAuth.getInstance()



    }

    override fun onResume(){
        super.onResume()
        btnCadastro.setOnClickListener {
            registrarUsuario()
        }


    }

    private fun registrarUsuario(){
        var email = ETemail.text.toString().trim()
        var senha = ETsenha.text.toString().trim()
        if(email.isEmpty()){
            ETemail.setError("O campo 'email' é obrigatório!")
            ETemail.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ETemail.setError("O Email que você digitou não é válido!")
            ETemail.requestFocus()
            return
        }
        if(senha.isEmpty()){
            ETsenha.setError("A senha precisa ser preenchida")
            ETsenha.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    val user : FirebaseUser? = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener{
                            task ->
                            if(task.isSuccessful){
                                Toast.makeText(this, "Registrado com sucesso", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, MainActivityKT::class.java))
                                finish()
                            }
                        }
                }else{
                    Toast.makeText(this, "Falha no registro", Toast.LENGTH_LONG).show()
                }
            }
    }
}