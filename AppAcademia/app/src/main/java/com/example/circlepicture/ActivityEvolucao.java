package com.example.circlepicture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class ActivityEvolucao extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    //TODO como lidar com datas aqui? Como agrupar fotos na mesma semana, dia, mes ou ano?
   

    FloatingActionButton btn_new_photo;
    Uri filePath;
    Uri photoURI;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    FirebaseUser user;
    FirebaseAuth auth;
    String id_user;

    LinearLayout layout;

    String currentPhotoPath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evolucao);

        BancoController bc = new BancoController(this);
        Cursor cursor = bc.getNumberOfImages(FirebaseAuth.getInstance().getUid());



        if(cursor.getCount()==0){
            TextView txtSemImagens = findViewById(R.id.txtSemImagens);
            txtSemImagens.setVisibility(View.VISIBLE);
        }else{
            ArrayList<String> imagesLinks = new ArrayList();
            do {
                if (cursor.getString(0) != null) {
                    imagesLinks.add(cursor.getString(0));
                }
            }while (cursor.moveToNext()) ;
            GridView gridView = findViewById(R.id.myGrid);
            gridView.setAdapter(new GalleryAdapter(imagesLinks, this));
        }

        btn_new_photo = findViewById(R.id.btn_newImage);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        id_user = user.getUid();


        btn_new_photo.setOnClickListener(v->{
            takePicture();
        });
    }

    private void colorrandom(ImageView imageView) {

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        imageView.setBackgroundColor(color);
    }

    private void addView(ImageView imageView, int width, int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        params.setMargins(0, 10, 0, 10);
        imageView.setLayoutParams(params);

        layout.addView(imageView);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if(intent.resolveActivity(getPackageManager()) !=null){
            //Create the file where the photo should go
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (IOException ex){
                //error ocurred while creating the file
            }
            //continue only if the file was successfully created
            if(photoFile != null){
                photoURI = FileProvider.getUriForFile(this, "com.example.circlepicture", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }

        //todo dar uma olhada no tutorial para salvar a imagem com alta resolu????o(link que ta no meu whats)


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //todo eu aparentemente gerei o arquivo com a foto em alta resolu????o mas eu acho que eu ainda tenho que configurar esse metodo
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
                filePath = Uri.parse(path);
                uploadImageToFirebaseStorage();

            }catch (FileNotFoundException e){
                Toast.makeText(this, "primeiro catch: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }catch (IOException e){
                Toast.makeText(this, "segundo catch: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        /*
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap bitmapReference = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            //bitmapReference.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmapReference, "Title", null);
            Toast.makeText(this, "path:  "+path, Toast.LENGTH_LONG).show();
            filePath = Uri.parse(path);
            uploadImageToFirebaseStorage();

            //aqui n??s temos a captura dos dados que comp??em a imagem
        }else{
            Toast.makeText(this, "os requests code s??o diferentes!!", Toast.LENGTH_LONG).show();
        }

         */






    }

    private File createImageFile() throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with action_view intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadImageToFirebaseStorage() {
        String nomeImagem = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("images/"+ nomeImagem);
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ActivityEvolucao.this, "imagem salva com sucesso no firebase", Toast.LENGTH_SHORT).show();
                BancoController bc = new BancoController(ActivityEvolucao.this);
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                //aqui eu posso usar o banco de dados para pegar o link ou os links das imagens e usar esse link para compor o gridView
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(ActivityEvolucao.this, ""+uri, Toast.LENGTH_LONG).show();
                        //download uri da imagem capturado com sucesso
                        //vamo ver como est?? o banco do meu appAcademia
                        bc.insertNewPhoto(id_user, date, nomeImagem, uri.toString());//TODO testar tudo isso///ta com algum erro
                        //testando os metodos do storagemetadata da referencia do storage ao tirar uma foto nova
                       ref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                           @Override
                           public void onSuccess(StorageMetadata storageMetadata) {
                               String tipoImagem = storageMetadata.getContentType();
                               Toast.makeText(ActivityEvolucao.this, "tipo da imagem: "+tipoImagem, Toast.LENGTH_SHORT).show();
                           }
                       })
                               .addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                        //n??o conseguiu pegar os metadados
                                   }
                               });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityEvolucao.this, "erro ao colocar a imagem no storage: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void downloadImage(){
        //esse m??todo talvez seja importante
    }
}

//codigo para criar imageViews automaticamente: https://stackoverflow.com/questions/29268976/how-to-automatically-create-imageview-in-android


