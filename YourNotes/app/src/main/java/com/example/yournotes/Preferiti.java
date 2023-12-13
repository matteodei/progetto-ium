package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Preferiti extends AppCompatActivity {

    public Button buttonBackFiles;
    LinearLayout containerLayout;
    private SharedPreferences sharedPreferences;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferiti);

        sharedPreferences = getSharedPreferences("preferiti", Context.MODE_PRIVATE);

        buttonBackFiles = (Button) findViewById(R.id.backButtonFiles);
        buttonBackFiles.setOnClickListener(view -> {
            setResult(YourFiles.RESULT_OK, new Intent());
            finish();
        });

        containerLayout = findViewById(R.id.containerLayout);
        dbHelper = new DatabaseHelper(this);

        String username = getIntent().getStringExtra("username");

        loadMateriePreferite(username);

    }

    @SuppressLint("Range")
    public void loadMateriePreferite(String username) {

        containerLayout.removeAllViews();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                CoursesContract.TABLE_NAME_PREFE,
                null,
                "NomeUtente=?",
                new String[]{username},
                null,
                null,
                null
        );

        ArrayList<String> materiaIDs = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String materiaID = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_MATERIA_PREFE));
                materiaIDs.add(materiaID);
            } while (cursor.moveToNext());

            cursor.close();
        }

        if (!materiaIDs.isEmpty()) {
            for (String materiaID : materiaIDs) {
                Log.d("materiaID", materiaID);
            }
        }

        if (!materiaIDs.isEmpty()) {
            for (String materiaID : materiaIDs) {
                cursor = db.query(
                        CoursesContract.TABLE_NAME,
                        null,
                        "_id=?",
                        new String[]{materiaID},
                        null,
                        null,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        @SuppressLint("InflateParams") View itemView = LayoutInflater.from(this).inflate(R.layout.items_layout, null);

                        Button followButton = itemView.findViewById(R.id.seguiButton);

                        followButton.setBackgroundResource(R.drawable.ic_preferiti_rosso);

                        TextView textViewNome = itemView.findViewById(R.id.nomeTextView);
                        TextView textViewCorso = itemView.findViewById(R.id.corsoLaureaTextView);
                        TextView textViewAnno = itemView.findViewById(R.id.annoTextView);
                        TextView textViewSemestre = itemView.findViewById(R.id.semestreTextView);
                        TextView textViewArgomenti = itemView.findViewById(R.id.argomentiTextView);
                        TextView textViewUser = itemView.findViewById(R.id.userTextView);

                        final int idIndex = cursor.getColumnIndex("_id");
                        final int itemID = cursor.getInt(idIndex);

                        String labelNome = "Materia: ";
                        String nome = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_NAME));
                        String labelCorso = "CdL: ";
                        String corso = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_CDL));
                        String labelAnno = "Anno: ";
                        String anno = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_YEAR));
                        String labelSemestre = "Semestre: ";
                        String semetre = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_SEMESTER));
                        String labelArgomenti = "";
                        String argomenti = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_TOPICS));
                        String segui = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_FOLLOW));
                        String user = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_USER));

                        followButton.setOnClickListener(v -> {
                            dbHelper.updateFollowState(username, itemID);
                            updateUI(username);
                        });

                        String label_nome = labelNome + nome;
                        SpannableString spannableStringNome = new SpannableString(label_nome);
                        spannableStringNome.setSpan(new StyleSpan(Typeface.BOLD), 0, labelNome.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        String label_cdl = labelCorso + corso;
                        SpannableString spannableStringCdL = new SpannableString(label_cdl);
                        spannableStringCdL.setSpan(new StyleSpan(Typeface.BOLD), 0, labelCorso.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        String label_anno = labelAnno + anno;
                        SpannableString spannableStringAnno = new SpannableString(label_anno);
                        spannableStringAnno.setSpan(new StyleSpan(Typeface.BOLD), 0, labelAnno.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        String label_semestre = labelSemestre + semetre;
                        SpannableString spannableStringSemestre = new SpannableString(label_semestre);
                        spannableStringSemestre.setSpan(new StyleSpan(Typeface.BOLD), 0, labelSemestre.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        String label_topic = labelArgomenti + argomenti;
                        SpannableString spannableStringArgomenti = new SpannableString(label_topic);
                        spannableStringArgomenti.setSpan(new StyleSpan(Typeface.BOLD), 0, labelArgomenti.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        textViewNome.setText(spannableStringNome);
                        textViewCorso.setText(spannableStringCdL);
                        textViewAnno.setText(spannableStringAnno);
                        textViewSemestre.setText(spannableStringSemestre);
                        textViewArgomenti.setText(spannableStringArgomenti);
                        textViewUser.setText(user);

                        containerLayout.addView(itemView);

                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }
        }

        db.close();

    }

    public void updateUI(String username){
        containerLayout.removeAllViews();

        loadMateriePreferite(username);
    }
}

