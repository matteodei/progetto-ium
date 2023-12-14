package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;

public class PDFViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfview);

        Button button = findViewById(R.id.backButton2);
        PDFView pdfView = findViewById(R.id.pdfView);

        button.setOnClickListener(view -> {
            setResult(YourFiles.RESULT_OK, new Intent());
            finish();
        });

        pdfView.fromAsset("filePDF.pdf").load();
    }
}