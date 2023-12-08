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


import com.example.yournotes.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import com.example.yournotes.databinding.ActivityMainBinding;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment(), username);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment(), username);
            } else if (item.getItemId() == R.id.profilo) {
                replaceFragment(new ProfileFragment(), username);
            } else if (item.getItemId() == R.id.aggiungi) {
                replaceFragment(new AggiungiFragment(), username);
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment, String username){

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


}