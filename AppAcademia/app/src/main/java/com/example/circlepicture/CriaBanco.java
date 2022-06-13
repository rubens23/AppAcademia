package com.example.circlepicture;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CriaBanco extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bancoAppAcademia.db";
    private static final int DATABASE_VERSION = 2;

    public CriaBanco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE userPhotos("
                +"user_id text, "
                +"date_of_image text, "
                +"image_id text primary key not null)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            db.execSQL("ALTER TABLE userPhotos ADD COLUMN link_imagem text");
        }

    }
}
