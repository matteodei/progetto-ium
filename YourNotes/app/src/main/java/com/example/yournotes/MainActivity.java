package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public Button button;
    public EditText email;
    public EditText password;
    public Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.createAccountButton);

        email = findViewById(R.id.editEmailLogin);
        password = findViewById(R.id.editPasswordLogin);
        loginButton = findViewById(R.id.accessButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Bundle bundle = getIntent().getExtras();
                    String rEmail = bundle.getString("email");
                    String rPassword = bundle.getString("password");

                    if (email.getText().toString().equals(rEmail) && password.getText().toString().equals(rPassword)) {
                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e) {
                    Toast.makeText(MainActivity.this, "Nessun Utente, registrati", Toast.LENGTH_SHORT).show();
                }

            }
        });




    }
}