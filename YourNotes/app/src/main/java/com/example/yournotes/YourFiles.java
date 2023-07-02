package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class YourFiles extends AppCompatActivity {

    public Button buttonBackFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_files);

        buttonBackFiles = (Button) findViewById(R.id.backButtonFiles);
        buttonBackFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YourFiles.this, Home.class);
                startActivity(intent);
            }
        });
    }
}