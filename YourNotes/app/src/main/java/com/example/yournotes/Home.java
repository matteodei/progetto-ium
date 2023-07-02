package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import java.io.FileWriter;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors


import com.example.yournotes.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.example.yournotes.databinding.ActivityMainBinding;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;


    public Button confirmButtonUpload;

    public EditText uCdl;
    public EditText uMateria;
    public EditText uDescrizione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.profilo) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.aggiungi) {
                replaceFragment(new AggiungiFragment());
            }
            return true;
        });

        uCdl = findViewById(R.id.getCdl);
        uMateria = findViewById(R.id.getMateria);
        uDescrizione = findViewById(R.id.getDescrizione);

        confirmButtonUpload = (Button)  findViewById(R.id.confirmAccountButton);
/*
        String cdl = uCdl.getText().toString().trim();
        String materia = uMateria.getText().toString().trim();
        String descrizione = uDescrizione.getText().toString().trim();



        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write(cdl+";"+materia+";"+descrizione);
            myWriter.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

         */



    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}