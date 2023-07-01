package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {

    public Button button;
    public Button confirmButton;

    public EditText rUsername;
    public EditText rEmail;
    public EditText rPassword;
    public EditText rConfirmPassword;
    public EditText rCorsoLaurea;

    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rUsername = findViewById(R.id.editNameRegister);
        rEmail = findViewById(R.id.editEmailRegister);
        rPassword = findViewById(R.id.editPasswordRegister);
        rConfirmPassword = findViewById(R.id.editConfirmPasswordRegister);
        rCorsoLaurea = findViewById(R.id.editCdLRegister);

        button = (Button) findViewById(R.id.backButton1);
        confirmButton = (Button) findViewById(R.id.confirmAccountButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Register.this, MainActivity.class);

                final String username = rUsername.getText().toString().trim();
                final String email = rEmail.getText().toString().trim();
                final String password = rPassword.getText().toString().trim();
                final String confirmPassword = rConfirmPassword.getText().toString().trim();
                final String CorsoLaure = rCorsoLaurea.getText().toString().trim();

                bundle.putString("email", email);
                bundle.putString("password", password);

                i.putExtras(bundle);

                startActivity(i);

            }
        });








    }
}