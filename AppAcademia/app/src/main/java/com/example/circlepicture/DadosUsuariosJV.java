package com.example.circlepicture;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DadosUsuariosJV extends AppCompatActivity {


    private TextView TVDados_nome, TVDados_email, TVDados_photoUrl, TVDados_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_usuarios);

        TVDados_nome = findViewById(R.id.TVDados_nome);
        TVDados_email = findViewById(R.id.TVDados_email);
        TVDados_photoUrl = findViewById(R.id.TVDados_photoUrl);
        TVDados_uid = findViewById(R.id.TVDados_uid);


        }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            // Name, email address, and profile photoUrl
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String uid = user.getUid();


            TVDados_nome.setText(""+name);
            TVDados_email.setText(""+email);
            TVDados_photoUrl.setText(""+photoUrl);
            TVDados_uid.setText(""+uid);

    }
}


}