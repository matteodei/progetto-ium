package com.example.yournotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.text.SpannableString;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    DatabaseHelper dbHelper;
    LinearLayout containerLayout;
    int flagMaterieSeguite;
    public HomeFragment() {}

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

        String username = getArguments().getString("username");
        SearchView searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Quando l'utente preme il tasto "Submit" nella tastiera
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Ogni volta che il testo nella SearchView cambia
                performSearch(newText);
                return false;
            }
        });

        //aka tuttoButton
        seguitiButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            seguitiButton.setBackgroundColor(Color.rgb(50,50,50));
            perTeButton.setBackgroundColor(Color.rgb(200,200,200));
            searchView.setQuery("", false);
            searchView.clearFocus();
            flagMaterieSeguite=1;
            cambioPaginaNome(username);
        });

        perTeButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            searchView.clearFocus();
            perTeButton.setBackgroundColor(Color.rgb(50,50,50));
            seguitiButton.setBackgroundColor(Color.rgb(200,200,200));
            searchView.setQuery("", false);
            searchView.clearFocus();
            flagMaterieSeguite=0;
            cambioPaginaNome(username);
        });

        perTeButton.setBackgroundColor(Color.rgb(50,50,50));
        seguitiButton.setBackgroundColor(Color.rgb(200,200,200));
        cambioPaginaNome(username);

        return rootView;
    }

    @SuppressLint("Range")
    public void cambioPaginaNome(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        containerLayout.removeAllViews();

        StringBuilder selection;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MaterieCheSegui", Context.MODE_PRIVATE);
        String materiePrefe = sharedPreferences.getString(username, "");
        String[] materiePrefeSplit = materiePrefe.split("£");

        if (flagMaterieSeguite == 0){
            selection = null;
            materiePrefeSplit = null;
        }
        else {

            selection = new StringBuilder();
            for (int i = 0; i < materiePrefeSplit.length; i++) {
                if (i > 0) {
                    selection.append(" OR ");
                }
                selection.append("nome=?");
            }
        }

        Cursor cursor;
        if (selection != null) {
            cursor = db.query(
                    CoursesContract.TABLE_NAME,
                    null,
                    selection.toString(),
                    materiePrefeSplit,
                    null,
                    null,
                    null
            );
        } else {

            cursor = db.query(
                    CoursesContract.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("InflateParams") View itemView = LayoutInflater.from(getContext()).inflate(R.layout.items_layout, null);

                AppCompatButton followButton = itemView.findViewById(R.id.seguiButton);
                AppCompatButton viewPdfButton = itemView.findViewById(R.id.visualizzaPdfTextView);
                TextView textViewNome = itemView.findViewById(R.id.nomeTextView);
                TextView textViewCorso = itemView.findViewById(R.id.corsoLaureaTextView);
                TextView textViewAnno = itemView.findViewById(R.id.annoTextView);
                TextView textViewSemestre = itemView.findViewById(R.id.semestreTextView);
                TextView textViewArgomenti = itemView.findViewById(R.id.argomentiTextView);
                TextView textViewUser = itemView.findViewById(R.id.userTextView);

                final int idIndex = cursor.getColumnIndex("_id");
                final int itemID = cursor.getInt(idIndex);
                ArrayList<String> idCorsiSeguiti = dbHelper.getPrefForUser(username);

                if( idCorsiSeguiti.contains(String.valueOf(itemID)))
                    followButton.setBackgroundResource(R.drawable.ic_preferiti_rosso);
                else
                    followButton.setBackgroundResource(R.drawable.ic_preferiti_vuoto);

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
                String user = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_USER));

                followButton.setOnClickListener(v -> {
                    dbHelper.updateFollowState(username, itemID);
                    //update UI
                    containerLayout.removeAllViews();
                    cambioPaginaNome(username);
                });

                viewPdfButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(),PDFViewActivity.class);
                    startActivity(intent);
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

        db.close();

    }

    @SuppressLint("Range")
    private void performSearch(String query) {

        containerLayout.removeAllViews();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String username = getArguments().getString("username");

        StringBuilder selection = new StringBuilder();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MaterieCheSegui", Context.MODE_PRIVATE);
        String materiePrefe = sharedPreferences.getString(username, "");
        String[] materiePrefeSplit = materiePrefe.split("£");

        Cursor cursor = null;

        if (flagMaterieSeguite == 0){
            selection.append("nome LIKE ?");

            cursor = db.query(
                    CoursesContract.TABLE_NAME,
                    null,
                    selection.toString(),
                    new String[]{"%" + query + "%"},
                    null,
                    null,
                    null
            );
        }
        else if (flagMaterieSeguite == 1) {

            selection = new StringBuilder();
            selection.append("( ");
            for (int i = 0; i < materiePrefeSplit.length; i++) {
                if (i > 0) {
                    selection.append(" OR ");
                }
                selection.append("nome=?");
            }
            selection.append(" ) AND nome LIKE ?");

            String[] selectionArgs = new String[materiePrefeSplit.length + 1];

            System.arraycopy(materiePrefeSplit, 0, selectionArgs, 0, materiePrefeSplit.length);
            selectionArgs[materiePrefeSplit.length] = "%" + query + "%";

            cursor = db.query(
                    CoursesContract.TABLE_NAME,
                    null,
                    selection.toString(),
                    selectionArgs,
                    null,
                    null,
                    null
            );
        }


        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("InflateParams") View itemView = LayoutInflater.from(getContext()).inflate(R.layout.items_layout, null);

                AppCompatButton followButton = itemView.findViewById(R.id.seguiButton);
                TextView textViewNome = itemView.findViewById(R.id.nomeTextView);
                TextView textViewCorso = itemView.findViewById(R.id.corsoLaureaTextView);
                TextView textViewAnno = itemView.findViewById(R.id.annoTextView);
                TextView textViewSemestre = itemView.findViewById(R.id.semestreTextView);
                TextView textViewArgomenti = itemView.findViewById(R.id.argomentiTextView);
                TextView textViewUser = itemView.findViewById(R.id.userTextView);

                final int idIndex = cursor.getColumnIndex("_id");
                final int itemID = cursor.getInt(idIndex);
                ArrayList<String> idCorsiSeguiti = dbHelper.getPrefForUser(username);


                if( idCorsiSeguiti.contains(String.valueOf(itemID)))
                    followButton.setBackgroundResource(R.drawable.ic_preferiti_rosso);
                else
                    followButton.setBackgroundResource(R.drawable.ic_preferiti_vuoto);

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
                String user = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_USER));

                followButton.setOnClickListener(v -> {
                    dbHelper.updateFollowState(username, itemID);
                    containerLayout.removeAllViews();
                    performSearch(query);
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

        db.close();
    }
}