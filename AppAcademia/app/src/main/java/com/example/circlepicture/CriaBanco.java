package com.example.circlepicture;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CriaBanco extends SQLiteOpenHelper {

    //todo n to achando o banco de dados nessa branch..

    private static final String DATABASE_NAME = "bancoAppAcademia.db";
    private static final int DATABASE_VERSION = 3;
    private Context ctx;

    public CriaBanco(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE userPhotos("
                +"user_id text, "
                +"date_of_image text, "
                +"image_id text primary key not null, "
                +"link_imagem text)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        if(newVersion > oldVersion){
            db.execSQL("ALTER TABLE userPhotos ADD COLUMN link_imagem text");
        }

         */

        if(newVersion > oldVersion){
            Toast.makeText(ctx, "to dentro do   onUpgrade do criaBanco", Toast.LENGTH_LONG).show();
        }

    }
}
