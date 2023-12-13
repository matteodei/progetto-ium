package com.example.yournotes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.SpannableString;
import java.util.Map;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    DatabaseHelper dbHelper;
    LinearLayout containerLayout;
    SharedPreferences sharedPreferences;

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

        String username = getArguments().getString("username");

        //aka tuttoButton
        seguitiButton.setOnClickListener(v -> {
            seguitiButton.setBackgroundColor(Color.rgb(50,50,50));
            perTeButton.setBackgroundColor(Color.rgb(200,200,200));

            cambioPaginaNome(1,username);
        });

        perTeButton.setOnClickListener(v -> {
            perTeButton.setBackgroundColor(Color.rgb(50,50,50));
            seguitiButton.setBackgroundColor(Color.rgb(200,200,200));

            cambioPaginaNome(0, username);
        });

        perTeButton.setBackgroundColor(Color.rgb(50,50,50));
        seguitiButton.setBackgroundColor(Color.rgb(200,200,200));
        cambioPaginaNome(0, username);

        return rootView;
    }

    @SuppressLint("Range")
    public void cambioPaginaNome(int flagSeguiti, String username) {

        containerLayout.removeAllViews();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder selection;
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MaterieCheSegui", Context.MODE_PRIVATE);
        String materiePrefe = sharedPreferences.getString(username, "");
        String[] materiePrefeSplit = materiePrefe.split("£");

        if (flagSeguiti == 0){
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

                final int idIndex = cursor.getColumnIndex("_id");
                final int itemID = cursor.getInt(idIndex);

                Button followButton = itemView.findViewById(R.id.seguiButton);
                TextView textViewNome = itemView.findViewById(R.id.nomeTextView);
                TextView textViewCorso = itemView.findViewById(R.id.corsoLaureaTextView);
                TextView textViewAnno = itemView.findViewById(R.id.annoTextView);
                TextView textViewSemestre = itemView.findViewById(R.id.semestreTextView);
                TextView textViewArgomenti = itemView.findViewById(R.id.argomentiTextView);
                TextView textViewFollow = itemView.findViewById(R.id.seguitaTextView);
                TextView textViewUser = itemView.findViewById(R.id.userTextView);

                String labelNome = "Corso: ";
                String nome = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_NAME));
                String labelCorso = "Cdl: ";
                String corso = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_CDL));
                String labelAnno = "Anno: ";
                String anno = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_YEAR));
                String labelSemestre = "Semestre: ";
                String semetre = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_SEMESTER));
                String labelArgomenti = "Topic: ";
                String argomenti = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_TOPICS));
                String segui = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_FOLLOW));
                String user = cursor.getString(cursor.getColumnIndex(CoursesContract.COLUMN_USER));

                followButton.setOnClickListener(v -> dbHelper.updateFollowState(username, itemID));

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
                textViewUser.setText(user);

                containerLayout.addView(itemView);

            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();

    }
}