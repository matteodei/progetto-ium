package com.example.yournotes;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editEmail, editCorsoLaurea, editConfirmPassword;
    private SharedPreferences sharedPreferences;

    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button = (Button) findViewById(R.id.backButton1);

        // Ottieni un riferimento alle SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        editTextUsername = findViewById(R.id.registerUsername);
        editTextPassword = findViewById(R.id.registerPassword);
        Button buttonRegister = findViewById(R.id.buttonRegister);

        editEmail = findViewById(R.id.registerEmail);
        editCorsoLaurea = findViewById(R.id.registerCorsoDiLaurea);
        editConfirmPassword = findViewById(R.id.registerConfirmPassword);

        TextInputLayout usernameLayout = findViewById(R.id.usernameLayout);
        TextInputLayout emailLayout = findViewById(R.id.emailLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
        TextInputLayout confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        TextInputLayout corsoLaureaLayout = findViewById(R.id.corsoLaureaLayout);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextUsername.clearFocus();
                editEmail.clearFocus();
                editTextPassword.clearFocus();
                editConfirmPassword.clearFocus();
                editCorsoLaurea.clearFocus();
                register();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
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

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emailLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        editCorsoLaurea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                corsoLaureaLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordLayout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        editConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPasswordLayout.setError(null);
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

        editConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Azioni da eseguire quando l'EditText ottiene il focus (viene cliccato)
                    confirmPasswordLayout.setError(null);
                } else {
                    // Azioni da eseguire quando l'EditText perde il focus
                }
            }
        });
    }

    private void register() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();
        String corsoLaurea = editCorsoLaurea.getText().toString().trim();

        String datiUtente = password + "£" + email + "£" + corsoLaurea;

        TextInputLayout usernameLayout = findViewById(R.id.usernameLayout);
        TextInputLayout emailLayout = findViewById(R.id.emailLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
        TextInputLayout confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        TextInputLayout corsoLaureaLayout = findViewById(R.id.corsoLaureaLayout);

        int controlloErrori = 0;

        // Verifica che il nome utente non sia vuoto o troppo lungo
        if (username.isEmpty()) {
            usernameLayout.setError("*Campo necessario");
        }else if(username.length() > 20){
            usernameLayout.setError("*Nome troppo lungo");
        }else if(isUserRegistered(username)){
            usernameLayout.setError("*Nome utente già in uso");
        }else{
            controlloErrori++;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("*Campo necessario");
        }else if(password.length() > 20){
            passwordLayout.setError("*Password troppo lunga");
        }else{
            controlloErrori++;
        }

        if (email.isEmpty()) {
            emailLayout.setError("*Campo necessario");
        }else if(email.length() > 20){
            emailLayout.setError("*Email troppo lunga");
        }else if(!isValidEmail(email)){
            emailLayout.setError("*Formato email non valido");
        }else{
            controlloErrori++;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.setError("*Campo necessario");
        }else if(confirmPassword.length() > 20){
            confirmPasswordLayout.setError("*Password troppo lunga");
        }else if(!(confirmPassword.equals(password))){
            confirmPasswordLayout.setError(("*Le password non conincidono"));
            passwordLayout.setError(("*Le password non conincidono"));
        }else{
            controlloErrori++;
        }

        if (corsoLaurea.isEmpty()) {
            corsoLaureaLayout.setError("*Campo necessario");
        }else if(corsoLaurea.length() > 20){
            corsoLaureaLayout.setError("*Nome troppo lungo");
        }else{
            controlloErrori++;
        }

        if(controlloErrori == 5) {
            // Verifica se l'utente esiste già (simulato hardcoded, da sostituire con una logica reale)

                // Registra il nuovo utente (simulato hardcoded, da sostituire con una logica reale)
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username, datiUtente);
                editor.apply();

                Toast.makeText(this, "Registrazione effettuata", Toast.LENGTH_SHORT).show();
                finish(); // Chiudi l'Activity di registrazione dopo il successo

        }
    }

    // Metodo per verificare se l'utente è già registrato
    private boolean isUserRegistered(String username) {
        return sharedPreferences.contains(username);
    }

    // Metodo per verificare il formato dell'email utilizzando una regex
    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}