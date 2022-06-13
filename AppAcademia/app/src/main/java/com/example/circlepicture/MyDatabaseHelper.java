package com.example.circlepicture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "appAcademia.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "cadastroUsuarios";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_SOBRENOME = "sobrenome";
    private static final String COLUMN_CID = "cidade";
    private static final String COLUMN_EST = "estado";
    private static final String COLUMN_PAIS = "pais";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_SENHA = "senha";


    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " ("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOME + " TEXT, " +
                COLUMN_SOBRENOME + " TEXT, " +
                COLUMN_CID + " TEXT, " +
                COLUMN_EST + " TEXT, " +
                COLUMN_PAIS + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_SENHA + " TEXT); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    void addAccount(String nome, String sobrenome, String cidade, String estado,
                    String pais, String email, String senha){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOME, nome);
        cv.put(COLUMN_SOBRENOME, sobrenome);
        cv.put(COLUMN_CID, cidade);
        cv.put(COLUMN_EST, estado);
        cv.put(COLUMN_PAIS, pais);
        cv.put(COLUMN_EMAIL, email);
        cv.put(COLUMN_SENHA, senha);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Falha no cadastro", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"Dados cadastrados com sucesso!", Toast.LENGTH_LONG).show();
        }
    }

    public Boolean checkEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from cadastroUsuarios where email = ?", new String[] {email});
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }
    //Fazer metodo check email password;
    public boolean checkEmailPassword(String email, String senha){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db .rawQuery("Select * from cadastroUsuarios where email = ? and senha = ?", new String[] {email, senha});
        if(cursor.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public Boolean updatepassword(String email, String senha){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SENHA, senha);
        long result = db.update(TABLE_NAME, cv, "email = ?", new String[] {email});
        if(result == -1){
            Toast.makeText(context, "ERRO 01: erro na alteração da senha.", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }


}

//ERRO 01: erro no update da senha antiga para a senha nova.
