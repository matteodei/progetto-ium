package com.example.yournotes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;

public class ProfileFragment extends Fragment {

    public Button buttonFiles;
    public Button logoutButton;
    public Button seguiMaterieButton;
    public Button preferitiButton;

    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttonFiles = (Button) view.findViewById(R.id.buttonAppunti);
        logoutButton = (Button) view.findViewById(R.id.logoutButton);
        seguiMaterieButton = (Button) view.findViewById(R.id.buttonSeguiMaterie);
        preferitiButton = (Button) view.findViewById(R.id.buttonPreferiti);

        seguiMaterieButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), SeguiMaterie.class);
            assert getArguments() != null;
            intent.putExtra("username", getArguments().getString("username"));
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });

        buttonFiles.setOnClickListener(view13 -> {
            Intent intent = new Intent(getActivity(), YourFiles.class);
            assert getArguments() != null;
            intent.putExtra("username", getArguments().getString("username"));
            startActivity(intent);
        });

        preferitiButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), Preferiti.class);
            assert getArguments() != null;
            intent.putExtra("username", getArguments().getString("username"));
            startActivity(intent);
        });

        // Ottieni un riferimento al contesto (Context) del fragment
        Context context = getContext();

        // Ottieni un riferimento alle SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        String username = getArguments().getString("username");

        String savedPassword = sharedPreferences.getString(username, "");
        String[] datiSeparati = savedPassword.split("£");

        TextView emailTextView = view.findViewById(R.id.provaEmail);
        TextView usernameTextView = view.findViewById(R.id.nomeUtente);
        TextView corsoTextView = view.findViewById(R.id.corsoSeguito);

        emailTextView.setText(datiSeparati[1]);
        usernameTextView.setText(datiSeparati[3]);
        corsoTextView.setText(datiSeparati[2]);
    }

}