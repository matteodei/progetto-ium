package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.example.yournotes.databinding.ActivityMainBinding;

public class Home extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button test = findViewById(R.id.bottomNavigationView);

        binding.


    }
}