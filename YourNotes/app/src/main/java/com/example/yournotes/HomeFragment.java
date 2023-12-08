package com.example.yournotes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.SpannableString;

public class HomeFragment extends Fragment {

    DatabaseHelper dbHelper;
    LinearLayout containerLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        containerLayout = rootView.findViewById(R.id.containerLayout);

        Button perTeButton = rootView.findViewById(R.id.perTeButton);
        Button seguitiButton = rootView.findViewById(R.id.seguitiButton);

        perTeButton.setOnClickListener(v -> cambioPagina(0));
        seguitiButton.setOnClickListener((v -> cambioPagina(1)));

        cambioPagina(1);


        return rootView;
    }

    public void updateFollowState(int id, int flagSeguiti) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String columnName = CoursesContract.COLUMN_FOLLOW;

        String selection = "_id=?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                CoursesContract.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String currentValue = cursor.getString(cursor.getColumnIndex(columnName));

                // Inverti il valore: se è 0, diventa 1 e viceversa
                String newValue = (currentValue.equals("0")) ? "1" : "0";

                // Aggiorna il valore solo per la riga specifica nell'ID fornito
                ContentValues values = new ContentValues();
                values.put(columnName, newValue);

                // Esegui l'update solo per la riga specifica dell'ID
                db.update(CoursesContract.TABLE_NAME, values, "_id=?", new String[]{String.valueOf(id)});

            } while (cursor.moveToNext());
            cursor.close();
        }

        cambioPagina(flagSeguiti);
    }

    public void cambioPagina(int flagSeguiti) {

        containerLayout.removeAllViews();

        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String selection = "seguita=?";
        String[] selectionArgs = {String.valueOf(flagSeguiti)};

        Cursor cursor = db.query(
                CoursesContract.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("InflateParams") View itemView = LayoutInflater.from(getContext()).inflate(R.layout.items_layout, null);

                Button followButton = itemView.findViewById(R.id.seguiButton);
                final int idIndex = cursor.getColumnIndex("_id");
                final int itemID = cursor.getInt(idIndex); // Ottieni l'ID corrente dal cursore
                followButton.setOnClickListener(v -> updateFollowState(itemID, flagSeguiti));
                TextView textViewNome = itemView.findViewById(R.id.nomeTextView);
                TextView textViewCorso = itemView.findViewById(R.id.corsoLaureaTextView);
                TextView textViewAnno = itemView.findViewById(R.id.annoTextView);
                TextView textViewSemestre = itemView.findViewById(R.id.semestreTextView);
                TextView textViewArgomenti = itemView.findViewById(R.id.argomentiTextView);
                TextView textViewFollow = itemView.findViewById(R.id.seguitaTextView);

                String labelNome = "Corso: ";
                @SuppressLint("Range") String nome = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_NAME));
                String labelCorso = "Cdl: ";
                @SuppressLint("Range") String corso = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_CDL));
                String labelAnno = "Anno: ";
                @SuppressLint("Range") String anno = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_YEAR));
                String labelSemestre = "Semestre: ";
                @SuppressLint("Range") String semetre = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_SEMESTER));
                String labelArgomenti = "Topic: ";
                @SuppressLint("Range") String argomenti = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_TOPICS));
                @SuppressLint("Range") String segui = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_FOLLOW));


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
                textViewFollow.setText(segui);

                containerLayout.addView(itemView);

            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();

    }
}