package com.example.circlepicture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {
    CriaBanco banco;
    SQLiteDatabase db;

    public BancoController(Context contexto){
        banco = new CriaBanco(contexto);
    }

    public String insertNewPhoto(String user_id, String date_of_image, String image_id, String link_img){
        long result;
        db = banco.getWritableDatabase();
        ContentValues valores;
        valores = new ContentValues();

        valores.put("user_id", user_id);
        valores.put("date_of_image", date_of_image);
        valores.put("image_id", image_id);
        valores.put("link_imagem", link_img);

        result = db.insert("userPhotos", null, valores);
        if(result == 1){
            return "Foto inserida com sucesso";
        }else{

            return "erro na inserção da foto";
        }

    }

    public Cursor getNumberOfImages(String user_id){
        Cursor cursor;
        String[] campos = {"link_imagem"};
        String where = "user_id='"+user_id+"'";
        db = banco.getReadableDatabase();
        cursor = db.query("userPhotos", campos, where, null, null, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;

    }

}
