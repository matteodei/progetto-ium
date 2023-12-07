package com.example.yournotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    public Button button;
    public Button loginButton;

    private EditText editTextUsername, editTextPassword;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ottieni un riferimento alle SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        button = (Button) findViewById(R.id.createAccountButton);

        editTextUsername = findViewById(R.id.editEmailLogin);
        editTextPassword = findViewById(R.id.editPasswordLogin);
        loginButton = findViewById(R.id.accessButton);

        TextInputLayout usernameLayout = findViewById(R.id.editUsernameLayout);
        TextInputLayout passwordLayout = findViewById(R.id.editPasswordLayout);

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
                editTextUsername.clearFocus();
                editTextPassword.clearFocus();
                login();
            }
        });

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                usernameLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Azioni da eseguire quando l'EditText ottiene il focus (viene cliccato)
                    passwordLayout.setError(null);
                } else {
                    // Azioni da eseguire quando l'EditText perde il focus
                }
            }
        });
    }

    private void login() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        TextInputLayout usernameLayout = findViewById(R.id.editUsernameLayout);
        TextInputLayout passwordLayout = findViewById(R.id.editPasswordLayout);

        String savedPassword = sharedPreferences.getString(username, "");
        String[] datiSeparati = savedPassword.split("£");

        int controlloErrori = 0;

        if(username.isEmpty()){
            usernameLayout.setError("*Campo necessario");
        }else if(savedPassword.isEmpty()){
            usernameLayout.setError("*Username sbagliato o inesistente");
        }else{
            controlloErrori++;
        }

        if(password.isEmpty()){
            passwordLayout.setError("*Campo necessario");
        }else if(!(datiSeparati[0].equals(password))){
            passwordLayout.setError("*Password sbagliata");
        }else{
            controlloErrori++;
        }

        if(controlloErrori == 2) {


                // Avvia la tua HomeActivity o un'altra Activity dopo il login
                Intent intent = new Intent(MainActivity.this, Home.class);
                intent.putExtra("username", username);
                startActivity(intent);

        }
    }

    // Metodo per verificare se l'utente è già loggato
    private boolean isUserLoggedIn() {
        return sharedPreferences.getString("username", null) != null;
    }

    public String getUsername(){
        String username = editTextUsername.getText().toString().trim();

        return username;
    }

}
