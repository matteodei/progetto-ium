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
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SeguiMaterie extends AppCompatActivity {

    public Button backButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences nomeMaterieSeguite;

    Map<Integer, Integer> indexMap = new HashMap<>();


    ListView listViewData;
    ArrayAdapter<String> adapter;
    String[] arrayMaterie = new String[]{
            "Analisi Matematica", "Architettura dei Calcolatori", "Biologia Molecolare", "Chimica",
            "Chimica Organica", "Diritto Civile", "Diritto del Lavoro", "Economia", "Elettronica",
            "Fisica", "Filologia Classica", "Filosofia", "Geografia Economica", "Geometria",
            "Ingegneria del Software", "Lingua Inglese", "Letteratura Italiana", "Microeconomia",
            "Programmazione 1", "Programmazione 2", "Psicologia", "Scienze Politiche", "Storia dell'Arte",
            "Storia Medievale", "Teoria dei Sistemi", "Statistica", "Algoritmi e Strutture Dati", "Fisiologia Umana",
            "Antropologia Culturale"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segui_materie);

        Intent intent = getIntent();

        // Ottieni un riferimento alle SharedPreferences
        nomeMaterieSeguite = getSharedPreferences("MaterieCheSegui", Context.MODE_PRIVATE);

        SearchView searchView = findViewById(R.id.SearchViewID);
        backButton = (Button) findViewById(R.id.backButton);

        backButton.setOnClickListener(view -> {
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
        });

        listViewData = findViewById(R.id.listView_data);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayMaterie);
        listViewData.setAdapter(adapter);

        listViewData.setOnItemClickListener((parent, view, position, id) -> {
            boolean checked = listViewData.isItemChecked(position);
            saveSelectionState(position, checked);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                performSearch(newText);
                return false;
            }
        });

        loadSelectionState();

    }

    private void performSearch(String query) {
        ArrayList<String> filteredList = new ArrayList<>();
        indexMap.clear(); // Pulisci la mappa degli indici

        if (query.isEmpty()) {
            filteredList.addAll(Arrays.asList(arrayMaterie));
        } else {
            for (int i = 0; i < arrayMaterie.length; i++) {
                String materia = arrayMaterie[i];
                if (materia.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(materia);
                    indexMap.put(filteredList.size() - 1, i); // Associa l'indice nell'elenco filtrato all'indice nell'elenco completo
                }
            }
        }

        ArrayAdapter<String> filteredAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, filteredList);
        listViewData.setAdapter(filteredAdapter);

        listViewData.setOnItemClickListener((parent, view, position, id) -> {
            boolean checked = listViewData.isItemChecked(position);
            saveSelectionState(position, checked);
        });

    }


    private void saveSelectionState(int position, boolean checked) {
        Integer originalIndex = indexMap.get(position);

        if (originalIndex != null) {
            int index = originalIndex.intValue();
            String utenteCorrente = getIntent().getStringExtra("username");
            String chiaveCombinata = index + "£" + utenteCorrente;
            nomeMaterieSeguite.edit().putBoolean(chiaveCombinata, checked).apply();
        } else {
            String utenteCorrente = getIntent().getStringExtra("username");
            String chiaveCombinata = position + "£" + utenteCorrente;
            nomeMaterieSeguite.edit().putBoolean(chiaveCombinata, checked).apply();
        }
    }



    private void loadSelectionState() {
        for (int i = 0; i < arrayMaterie.length; i++) {
            String utenteCorrente = getIntent().getStringExtra("username");
            String chiaveCombinata = i + "£" + utenteCorrente;
            boolean isSelected = nomeMaterieSeguite.getBoolean(chiaveCombinata, false);
            listViewData.setItemChecked(i, isSelected);
        }
    }

}
