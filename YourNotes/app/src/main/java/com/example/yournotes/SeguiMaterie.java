package com.example.yournotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.strictmode.GetTargetFragmentRequestCodeUsageViolation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Map;

public class SeguiMaterie extends AppCompatActivity {

    public Button backButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences nomeMaterieSeguite;
    ListView listViewData;
    ArrayAdapter<String> adapter;
    String[] arrayMaterie = {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segui_materie);

        Intent intent = getIntent();

        // Ottieni un riferimento alle SharedPreferences
        sharedPreferences = getSharedPreferences("MaterieSeguite", Context.MODE_PRIVATE);
        nomeMaterieSeguite = getSharedPreferences("MaterieCheSegui", Context.MODE_PRIVATE);

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemSelected = "";
                for(int i = 0; i<listViewData.getCount(); i++){
                    if(listViewData.isItemChecked(i)){
                        itemSelected += listViewData.getItemAtPosition(i) + "£";
                    }
                }
                SharedPreferences.Editor editor = nomeMaterieSeguite.edit();
                editor.putString(intent.getStringExtra("username"), itemSelected);
                editor.apply();
                Intent returnIntent = new Intent();
                setResult(YourFiles.RESULT_OK, returnIntent);

                finish();
            }
        });

        String currentSP = sharedPreferences.getString(intent.getStringExtra("username"), "");
        String[] datiSeparati = currentSP.split("£");

        listViewData = findViewById(R.id.listView_data);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayMaterie);
        listViewData.setAdapter(adapter);

        listViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean checked = listViewData.isItemChecked(position);
                saveSelectionState(position, checked);
            }
        });
        // BUG MULTIUSER: QUANDO SWITCHO UTENTE E VIENE RICARICATO LO STATO DELLE CHECKED VENGONO
        // ASSEGNATE QUELLE DELL'ALTRO UTENTE APPENA SLOGGATO.
        // Carica lo stato delle selezioni quando l'activity viene avviata
        loadSelectionState();

    }

    private void saveSelectionState(int position, boolean checked) {
        // Salva lo stato dell'elemento nella SharedPreferences
        sharedPreferences.edit().putBoolean(String.valueOf(position), checked).apply();
    }

    private void loadSelectionState() {
        for (int i = 0; i < arrayMaterie.length; i++) {
            boolean isSelected = sharedPreferences.getBoolean(String.valueOf(i), false);
            listViewData.setItemChecked(i, isSelected);
        }
    }

}
