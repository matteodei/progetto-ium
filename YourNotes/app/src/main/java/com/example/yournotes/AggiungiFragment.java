package com.example.yournotes;

import static android.app.Activity.RESULT_OK;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.LogPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AggiungiFragment extends Fragment {

    private static final int PICK_PDF_FILE = 1;
    String[] arrayMaterie = new String[]{
            "Analisi Matematica", "Architettura dei Calcolatori", "Biologia Molecolare", "Chimica",
            "Chimica Organica", "Diritto Civile", "Diritto del Lavoro", "Economia", "Elettronica",
            "Fisica", "Filologia Classica", "Filosofia", "Geografia Economica", "Geometria",
            "Ingegneria del Software", "Lingua Inglese", "Letteratura Italiana", "Microeconomia",
            "Programmazione 1", "Programmazione 2", "Psicologia", "Scienze Politiche", "Storia dell'Arte",
            "Storia Medievale", "Teoria dei Sistemi", "Statistica", "Programmazione 2", "Analisi Matematica",
            "Filosofia", "Geometria", "Fisica", "Chimica", "Storia dell'Arte", "Letteratura Italiana",
            "Economia", "Ingegneria del Software", "Algoritmi e Strutture Dati", "Fisiologia Umana",
            "Lingua Inglese", "Storia Medievale", "Diritto Civile", "Architettura dei Calcolatori",
            "Biologia Molecolare", "Scienze Politiche", "Statistica", "Psicologia", "Geografia Economica",
            "Chimica Organica", "Filologia Classica", "Antropologia Culturale", "Elettronica",
            "Teoria dei Sistemi", "Microeconomia", "Diritto del Lavoro"
    };

    String[] arrayMaterieSenzaDuplicati = new String[]{
            "Analisi Matematica", "Architettura dei Calcolatori", "Biologia Molecolare", "Chimica",
            "Chimica Organica", "Diritto Civile", "Diritto del Lavoro", "Economia", "Elettronica",
            "Fisica", "Filologia Classica", "Filosofia", "Geografia Economica", "Geometria",
            "Ingegneria del Software", "Lingua Inglese", "Letteratura Italiana", "Microeconomia",
            "Programmazione 1", "Programmazione 2", "Psicologia", "Scienze Politiche", "Storia dell'Arte",
            "Storia Medievale", "Teoria dei Sistemi", "Statistica", "Algoritmi e Strutture Dati", "Fisiologia Umana",
            "Antropologia Culturale"
    };

    String[] arrayAnni = new String[]{"Sel. Anno", "1", "2", "3"};
    String[] arraySemestri = new String[]{"Sel. Semestre", "1", "2"};

    private DatabaseHelper dbHelper;
    AutoCompleteTextView autoCompleteTextView;
    Spinner spinnerAnno;
    Spinner spinnerSemestre;

    public AggiungiFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_aggiungi, container, false);

        autoCompleteTextView = rootView.findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, arrayMaterieSenzaDuplicati);
        autoCompleteTextView.setAdapter(adapter);

        spinnerAnno = rootView.findViewById(R.id.spinnerAnno);
        ArrayAdapter<String> adapterAnno = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, arrayAnni);
        adapterAnno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnno.setAdapter(adapterAnno);

        spinnerSemestre = rootView.findViewById(R.id.spinnerSemestre);
        ArrayAdapter<String> adapterSemestre = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, arraySemestri);
        adapterSemestre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSemestre.setAdapter(adapterSemestre);


        dbHelper = new DatabaseHelper(requireContext());

        Button buttonConferma = rootView.findViewById(R.id.buttonConferma);
        buttonConferma.setOnClickListener(v -> confermaInserimento());

        //Button buttonElimina = rootView.findViewById(R.id.buttonElimina);
        //buttonElimina.setOnClickListener(v -> eliminaDatabase());

        Button buttonSelectPDF = rootView.findViewById(R.id.buttonSelectPDF);

        buttonSelectPDF.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                startActivityForResult(Intent.createChooser(intent, "Seleziona un file PDF"), PICK_PDF_FILE);
            } catch (android.content.ActivityNotFoundException ex) {
                // Gestisci l'eccezione, se necessario
            }
        });

        return rootView;
    }

    private void confermaInserimento() {

        autoCompleteTextView = requireView().findViewById(R.id.autoCompleteTextView);
        EditText editTextCdL = requireView().findViewById(R.id.editTextCdL);
        spinnerAnno = requireView().findViewById(R.id.spinnerAnno);
        spinnerSemestre = requireView().findViewById(R.id.spinnerSemestre);
        EditText editTextCourseTopics = requireView().findViewById(R.id.editTextCourseTopics);

        String nome = autoCompleteTextView.getText().toString();
        String corsoDiLaurea = editTextCdL.getText().toString();
        String anno = spinnerAnno.getSelectedItem().toString();
        String semestre = spinnerSemestre.getSelectedItem().toString();
        String argomenti = editTextCourseTopics.getText().toString();
        String username = getArguments().getString("username");

        TextInputLayout materiaLayout = requireView().findViewById(R.id.editTextNameLayout);
        TextInputLayout cdlLayout = requireView().findViewById(R.id.editTextCdLLayout);
        TextInputLayout topicsLayout = requireView().findViewById(R.id.editTextCourseTopicsLayout);

        TextView helperTextAnno = requireView().findViewById(R.id.helperTextAnno);
        TextView helperTextSemestre = requireView().findViewById(R.id.helperTextSemestre);

        int controlloErrori = 0;

        if (nome.isEmpty()) {
            materiaLayout.setError("*Campo necessario");
        }else{
            for(int i = 0; i<arrayMaterieSenzaDuplicati.length; i++){
                if((nome.equals(arrayMaterieSenzaDuplicati[i])) && (controlloErrori == 0)){
                    controlloErrori++;
                }
            }
            if(controlloErrori == 0){
                materiaLayout.setError("*La materia deve essere una di quelle seguibili");
            }
        }

        if (corsoDiLaurea.isEmpty()) {
            cdlLayout.setError("*Campo necessario");
        }else if(corsoDiLaurea.length() > 20){
            cdlLayout.setError("*Nome troppo lungo");
        }else{
            controlloErrori++;
        }

        if (argomenti.isEmpty()) {
            topicsLayout.setError("*Campo necessario");
        }else{
            controlloErrori++;
        }

        if(!(anno.equals("Sel. Anno"))){
            controlloErrori++;
        }else{
            spinnerAnno.setBackgroundResource(R.drawable.custom_spinner_error);
            helperTextAnno.setVisibility(View.VISIBLE);
            helperTextAnno.setText("*Selezionare");
        }

        if(!(semestre.equals("Sel. Semestre"))){
            controlloErrori++;
        }else{
            spinnerSemestre.setBackgroundResource(R.drawable.custom_spinner_error);
            helperTextSemestre.setVisibility(View.VISIBLE);
            helperTextSemestre.setText("*Selezionare");
        }

        if (controlloErrori == 5) {

            ContentValues values = new ContentValues();
            values.put(CoursesContract.COLUMN_NAME, nome);
            values.put(CoursesContract.COLUMN_CDL, corsoDiLaurea);
            values.put(CoursesContract.COLUMN_YEAR, anno);
            values.put(CoursesContract.COLUMN_SEMESTER, semestre);
            values.put(CoursesContract.COLUMN_TOPICS, argomenti);
            values.put(CoursesContract.COLUMN_FOLLOW, "0");
            values.put(CoursesContract.COLUMN_USER, username);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.insert(CoursesContract.TABLE_NAME, null, values);

            db.close();

            autoCompleteTextView.setText("");
            editTextCdL.setText("");
            editTextCourseTopics.setText("");

            Toast.makeText(requireContext(), "Corso inserito con successo", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(requireContext(), "Riempi tutti i campi", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminaDatabase() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(CoursesContract.TABLE_NAME, null, null);

        db.close();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            // Ora hai l'URI del file PDF selezionato, puoi fare ciò che desideri con esso
            Log.d("--URI--", String.valueOf(uri));
        }
    }

}