package com.example.circlepicture

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_main.*

class MainActivityKT : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference



        textViewCadastro.setOnClickListener {
            startActivity(Intent(this, paginaRegistroKT::class.java))
            finish()
        }
        btnLogin.setOnClickListener{
            fazerLogin()
        }

    }

    private fun fazerLogin(){
        auth = FirebaseAuth.getInstance()
        val LoginEmail = ETEmailLogin.text.toString().trim()
        var LoginSenha = ETSenhaLogin.text.toString().trim()
        if(LoginEmail.isEmpty()){
            ETEmailLogin.setError("O Email precisa ser preenchido!")
            ETEmailLogin.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(LoginEmail).matches()){
            ETEmailLogin.setError("O Email que você digitou não é válido!")
            ETEmailLogin.requestFocus()
            return
        }
        if(LoginSenha.isEmpty()){
            ETSenhaLogin.setError("A senha não foi preenchida")
            ETSenhaLogin.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(LoginEmail, LoginSenha)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    //if sign in succeeds
                    val user : FirebaseUser? = auth.currentUser
                    updateUI(user)
                }else{
                    //if sign in fails
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?){

        if(currentUser != null){
            if(currentUser.isEmailVerified){
                startActivity(Intent(this, homeActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Seu email ainda não foi verificado", Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this, "Falha no Login", Toast.LENGTH_LONG).show()
        }
    }



}

//Acho que o user nao ta recebendo null aque na "loginactivity"
//ele deve estar recebendo null na home activity


