package com.example.circlepicture;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityChangeProfilePicture extends AppCompatActivity{

    private FirebaseAuth mAuth;

    private CircleImageView btnChooseImage;
    private Button btnVoltar, btnSalvar;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code, why is it 22?
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_picture);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();//o mesmo que storageRef do tutorial
        mAuth = FirebaseAuth.getInstance();



        btnVoltar = findViewById(R.id.btnVoltar);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnChooseImage = findViewById(R.id.change_profile_photo);

        //colocar a imagem atual do usuario aqui no
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Uri photoURIInicial = currentUser.getPhotoUrl();
        if(photoURIInicial != null) {
            String photoUrlIni = currentUser.getPhotoUrl().toString();
            Glide.with(ActivityChangeProfilePicture.this).load(photoUrlIni).into(btnChooseImage);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityChangeProfilePicture.this, homeActivity.class));
            }
        });
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

    }

    //Select Image method
    private void SelectImage(){
        //Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //code to set selected image in method
        //SelectImage() to CircleImageView
        if(requestCode == PICK_IMAGE_REQUEST
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null){

            // Get the Uri of data
            filePath = data.getData();
            try{
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                btnChooseImage.setImageBitmap(bitmap);
            }
            catch(IOException e){
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    private void uploadImage(){
        if(filePath != null){
            //Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Salvando Imagem de Perfil...");
            progressDialog.show();



            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // Dismis dialog
                            progressDialog.dismiss();
                            Toast.makeText(ActivityChangeProfilePicture.this, "Imagem Salva!!", Toast.LENGTH_SHORT).show();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String resultadoNome = ref.getName();
                            Toast.makeText(ActivityChangeProfilePicture.this, ""+resultadoNome, Toast.LENGTH_LONG).show();
                            FirebaseUser updateUserProfilePic = FirebaseAuth.getInstance().getCurrentUser();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    String linkImagem = downloadUrl.toString();
                                    Toast.makeText(ActivityChangeProfilePicture.this, linkImagem, Toast.LENGTH_LONG).show();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(Uri.parse(linkImagem))//tentar passar uma Uri aqui
                                            .build();
                                    currentUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d(TAG, "User profile updated!");
                                            }
                                        }
                                    });



                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Error, Image not uploaded
                                            progressDialog.dismiss();
                                            Toast.makeText(ActivityChangeProfilePicture.this, "Falha no salvamento da Imagem", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                        }
                    });






        }
    }
}

//n ta atualizando os dados nem a imagem do menu
