package com.example.circlepicture;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.circlepicture.databinding.ActivityEvolucaoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ActivityEvolucao extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoUri;
    private Uri uriPathForStoringImageInFirebaseStorage;

    FloatingActionButton btn_new_photo;

    private ActivityEvolucaoBinding binding;
    private SimpleAdapter mAdapter;
    private BancoController bc;

    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    private String currentTakenPictureStringPath;
    private String id_user;

    //todo se o user ainda não tiver tirado nenhuma foto mostrar uma mensagem no centro da tela dizendo: "tire uma foto para ter um registro da mudança do seu corpo com os exercícios"
    //todo se o user já tiver tirado alguma foto eu tenho que carregar a foto com sua devida categoria, que no caso é um grupo de intervalo de datas(hoje, na ultima semana, nos ultimos 6 meses etc)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEvolucaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initMembers();
        btn_new_photo = findViewById(R.id.btn_newImage);
        btn_new_photo.setOnClickListener(v->{
            takePicture();
        });
        setImagesInAdapter();
        setCategoriesSections();

    }

    private void managingRecycler(List<String> listaPhotos) {
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 4));

        //Your recyclerview adapter
        mAdapter = new SimpleAdapter(this, listaPhotos);

    }

    private void initMembers() {
        bc = new BancoController(this);
        id_user = FirebaseAuth.getInstance().getUid();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bitmap bitmap;
            try{
                //preparar para passar a imagem recém tirada para o Storage
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Title", null);
                uriPathForStoringImageInFirebaseStorage = Uri.parse(path);
                uploadImageToFirebaseStorage();
            }catch(FileNotFoundException e){
                Log.d("OnActivityResult", "FileNotFound: "+ e.getMessage());
            }catch(IOException e){
                Log.d("OnActivityResult", "IOException: "+ e.getMessage());
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        String nomeImagem = UUID.randomUUID().toString();
        StorageReference firebaseStorageReference = storageReference.child("images/"+nomeImagem);
        firebaseStorageReference.putFile(uriPathForStoringImageInFirebaseStorage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ActivityEvolucao.this, "imagem salva com sucesso no firebase", Toast.LENGTH_SHORT).show();
                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                getRecentlyTakenPictureDownloadLink(firebaseStorageReference, date, nomeImagem);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActivityEvolucao.this, "Erro ao colocar imagem no storage", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getRecentlyTakenPictureDownloadLink(StorageReference firebaseStorageReference, String date, String nomeImagem) {
        firebaseStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                bc.insertNewPhoto(id_user, date, nomeImagem, uri.toString());
                //getRecentlyTakenPictureMetadata(firebaseStorageReference);
            }
        });
    }

    private void getRecentlyTakenPictureMetadata(StorageReference firebaseStorageReference) {
        firebaseStorageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                String tipoImagem = storageMetadata.getContentType();
                Toast.makeText(ActivityEvolucao.this, "tipo da imagem: "+tipoImagem, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //não conseguiu pegar os metadados da imagem
            }
        });
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //get high res image file
        if(intent.resolveActivity(getPackageManager()) != null){
            //Create the file where the photo should go
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException ex){
                //error ocurred while creating the image file
            }
            //continue only if the file was successfully created
            if(photoFile != null){
                photoUri = FileProvider.getUriForFile(this, "com.example.circle", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        //create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        //Save a file: path for use with action_view intents
        currentTakenPictureStringPath = image.getAbsolutePath();
        return image;
    }

    private void setImagesInAdapter() {
        Cursor imagesSavedInDatabase = bc.getNumberOfImages(FirebaseAuth.getInstance().getUid());

        if(imagesSavedInDatabase.getCount()==0){
            TextView txtSemImagens = findViewById(R.id.txtSemImagens);
            txtSemImagens.setVisibility(View.VISIBLE);
        }else{
            ArrayList<String> imageLinks = new ArrayList();
            do{
                if(imagesSavedInDatabase.getString(0) != null){
                    //adiciona link da imagem a imageLinks
                    imageLinks.add(imagesSavedInDatabase.getString(0));
                }
            }while (imagesSavedInDatabase.moveToNext());
            managingRecycler(imageLinks);
        }
    }

    private void setCategoriesSections() {
        List<SectionedGridRecyclerViewAdapter.Section> sections = new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

        sections.add(new SectionedGridRecyclerViewAdapter.Section(0,"hoje"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(5,"na última semana"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(12,"no último mês"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(14,"nos últimos 6 meses"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(20,"há mais de um ano"));

        //add your adapter to the sectionAdapter
        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                SectionedGridRecyclerViewAdapter(this,R.layout.section,R.id.section_text,binding.recycler,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        binding.recycler.setAdapter(mSectionedAdapter);

    }

}

//link sectiloned grid:
//https://gist.github.com/gabrielemariotti/e81e126227f8a4bb339c
//testar essa solução em um projeto novo, antes de implementar aqui, veja se essa
//solução suportará a adição de novas imagens e se tudo vai ser atualizado certinho
//link do stackoverflow onde eu achei essa possível solução:
//https://stackoverflow.com/questions/7397988/android-gridview-with-categories


