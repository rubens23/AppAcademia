package com.example.circlepicture;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

import de.hdodenhof.circleimageview.CircleImageView;

public class homeActivity extends AppCompatActivity {

    Button btnActivityImc, btnDados, btnEvolucao;
    CircleImageView homeProfile;

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;

    //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();


        btnActivityImc = findViewById(R.id.btnActivityImc);
        btnDados = findViewById(R.id.btnDados);
        homeProfile = findViewById(R.id.homeActivityProfileImage);
        btnEvolucao = findViewById(R.id.btnEvolucao);

        mAuth = FirebaseAuth.getInstance();









    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            downloadAndSetImage();
            Uri photoURI = user.getPhotoUrl();
            if (photoURI != null) {
                String photoUrl = user.getPhotoUrl().toString();
            }
            btnActivityImc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), CalculoImc.class);
                    startActivity(intent);
                }
            });

            btnDados.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showUserData();

                }
            });

            homeProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentTemplateProfile = new Intent(homeActivity.this, ActivityChangeProfilePicture.class);
                    startActivity(intentTemplateProfile);
                }
            });

            //se foto do usuario tiver sido alterada carrega o metodo de download da imagem denovo
            Uri photoUrlChange = user.getPhotoUrl();
            if(photoURI != null) {
                if (photoUrlChange.equals(photoURI)) {
                    //imagem não foi atualizada
                } else {
                    String photoChange = user.getPhotoUrl().toString();
                    Glide.with(this).load(photoChange).into(homeProfile);
                }
            }
        }

        btnEvolucao.setOnClickListener(v->{
            startActivity(new Intent(this, ActivityEvolucao.class));
        });


    }

    private void downloadAndSetImage(){
        FirebaseUser user = mAuth.getCurrentUser();
        //get user's profile photo url
        if (user != null) {
            //get image url

            Uri photoUrl = user.getPhotoUrl();
            //set image using glide
            if (photoUrl != null) {
                String photoUrlstr = user.getPhotoUrl().toString();
                Glide.with(this).load(photoUrlstr).into(homeProfile);
            }
        }

        /*
        //set image view with user's profile photo
        long ONE_MEGABYTE = 1024*1024;

        storageReference.child("images/6db06554-1de8-4b1c-9a3c-e66125ef86aa").getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        homeProfile.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        Toast.makeText(homeActivity.this, "FileDownloaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(homeActivity.this, "Download error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "OnFailure: Error: "+e.getMessage());
                    }
                });

         */

    }

    private void showUserData() {
        startActivity(new Intent(this, DadosUsuariosJV.class));
    }


}

//entrou mas agora deu erro para ir para a editprofile


