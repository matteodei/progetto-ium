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
        rCorsoLaurea = findViewById(R.id.uploadFile);

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
                // Ottenere i valori dai campi di input
                String username = rUsername.getText().toString().trim();
                String email = rEmail.getText().toString().trim();
                String corsoLaurea = rCorsoLaurea.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String confirmPassword = rConfirmPassword.getText().toString().trim();

                // Validazione dei campi
                if (username.isEmpty()) {
                    rUsername.setError("Il campo username è obbligatorio");
                    rUsername.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    rEmail.setError("Il campo email è obbligatorio");
                    rEmail.requestFocus();
                    return;
                }

                if (corsoLaurea.isEmpty()) {
                    rCorsoLaurea.setError("Il campo corso di laurea è obbligatorio");
                    rCorsoLaurea.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    rPassword.setError("Il campo password è obbligatorio");
                    rPassword.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    rConfirmPassword.setError("Il campo conferma password è obbligatorio");
                    rConfirmPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    rConfirmPassword.setError("Le password non combaciano");
                    rConfirmPassword.requestFocus();
                    return;
                }

                // Se tutti i controlli passano, puoi procedere con l'azione successiva, ad esempio il salvataggio dei dati o l'invio di una richiesta.
                Intent i = new Intent(Register.this, MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("email", email);
                bundle.putString("password", password);
                i.putExtras(bundle);

                startActivity(i);
            }
        });


    }
}