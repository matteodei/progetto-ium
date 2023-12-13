package com.example.yournotes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CorsiDB6";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableQuery = "CREATE TABLE Corsi (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "cdl TEXT, " +
                "anno TEXT, " +
                "semestre TEXT, " +
                "argomenti TEXT," +
                "seguita TEXT,"+
                "user TEXT)";
        db.execSQL(createTableQuery);

        String createPreferredCourseTable = "CREATE TABLE CorsiPreferiti (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NomeUtente TEXT," +
                "MateriaPreferita INTEGER, " +
                "FOREIGN KEY(MateriaPreferita) REFERENCES Corsi(_id)" +
                ")";

        db.execSQL(createPreferredCourseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Azioni per l'aggiornamento del database (se necessario)
    }

    public void aggiungiPreferiti(String username, int idCorsoPreferito) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NomeUtente", username);
        values.put("MateriaPreferita", idCorsoPreferito);

        db.insert("CorsiPreferiti", null, values);

    }

    public void rimuoviPreferiti(String username, int idCorsoPreferito) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CorsiPreferiti", "NomeUtente=? AND MateriaPreferita=?",
                new String[]{username, String.valueOf(idCorsoPreferito)});

    }
    @SuppressLint("Range")
    public void updateFollowState(String username, int idCorsoPreferito) {
        /*
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = "NomeUtente=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                CoursesContract.TABLE_NAME_PREFE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String currentValue = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_MATERIA_PREFE));
                Log.d("currentValue", currentValue);

                if (currentValue.equals(corsoPreferito)) {

                }else {

                }

            } while (cursor.moveToNext());
            cursor.close();
        }else {
            aggiungiPreferiti(username, corsoPreferito);
        }*/
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("DATI che mi arrivano dal bottone premuto",username + ' ' + idCorsoPreferito);

        Cursor cursor = db.query(
                "CorsiPreferiti",
                null,
                "NomeUtente=? AND MateriaPreferita=?",
                new String[]{username, String.valueOf(idCorsoPreferito)},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Se la materia preferita esiste già, la rimuovi
            rimuoviPreferiti(username, idCorsoPreferito);
        } else {
            // Se la materia preferita non esiste, la aggiungi
            aggiungiPreferiti(username, idCorsoPreferito);
        }

        cursor = db.query(
                CoursesContract.TABLE_NAME_PREFE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String currentValuename = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_NAME_PREFE));
                String currentValue = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_MATERIA_PREFE));
                Log.d("DATI DATABASE", currentValuename + ' ' + currentValue);

            } while (cursor.moveToNext());
            cursor.close();

        }

        db.close();
    }

}

